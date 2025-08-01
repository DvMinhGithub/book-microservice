package com.mdv.identity.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mdv.identity.dto.request.AuthenticationRequest;
import com.mdv.identity.dto.request.IntrospectRequest;
import com.mdv.identity.dto.request.LogoutRequest;
import com.mdv.identity.dto.request.RefreshRequest;
import com.mdv.identity.dto.response.AuthenticationResponse;
import com.mdv.identity.dto.response.IntrospectResponse;
import com.mdv.identity.entity.InvalidatedToken;
import com.mdv.identity.entity.User;
import com.mdv.identity.exception.ApiErrorCode;
import com.mdv.identity.exception.ApiException;
import com.mdv.identity.exception.JWTSigningException;
import com.mdv.identity.repository.InvalidatedTokenRepository;
import com.mdv.identity.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerkey}")
    protected String singerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected int validDuration;

    @NonFinal
    @Value("${jwt.refresh-duration}")
    protected int refreshDuration;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean valid = true;

        try {
            verifyToken(token, false);
        } catch (ApiException e) {
            valid = false;
        }

        return IntrospectResponse.builder().valid(valid).build();
    }

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException(ApiErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new ApiException(ApiErrorCode.PASSWORD_INCORRECT);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token.token)
                .expiryTime(token.expiryDate)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            verifyAndSaveInvalidToken(request.getToken(), false);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedToken = verifyAndSaveInvalidToken(request.getToken(), true);

        String username = signedToken.getJWTClaimsSet().getSubject();
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ApiException(ApiErrorCode.USER_NOT_EXISTED));

        var newToken = generateToken(user);

        return AuthenticationResponse.builder()
                .token(newToken.token)
                .expiryTime(newToken.expiryDate)
                .build();
    }

    SignedJWT verifyAndSaveInvalidToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        var signedToken = verifyToken(token, isRefresh);

        String jwtId = signedToken.getJWTClaimsSet().getJWTID();
        Date expireTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jwtId).expiryTime(expireTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        return signedToken;
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(singerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expireTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(refreshDuration, ChronoUnit.HOURS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify = signedJWT.verify(verifier);

        if (!(verify && expireTime.after(new Date()))) {
            throw new ApiException(ApiErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new ApiException(ApiErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private TokenInfo generateToken(User user) {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();

        Date issueTime = new Date();
        Date expiryTime =
                new Date(Instant.now().plus(validDuration, ChronoUnit.HOURS).toEpochMilli());

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("mdv")
                .issueTime(issueTime)
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .build();

        Payload payload = new Payload(claims.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(singerKey));
            return new TokenInfo(jwsObject.serialize(), expiryTime);
        } catch (JOSEException e) {
            throw new JWTSigningException("Error signing JWT token");
        }
    }

    String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());

                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
            });

        return joiner.toString();
    }

    private record TokenInfo(String token, Date expiryDate) {}
}
