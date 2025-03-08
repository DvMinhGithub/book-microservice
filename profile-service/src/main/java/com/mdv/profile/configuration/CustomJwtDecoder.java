package com.mdv.profile.configuration;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            String tokenValue = signedJWT.getParsedString();
            Instant issuedAt = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            Instant expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            Map<String, Object> headers = signedJWT.getHeader().toJSONObject();
            Map<String, Object> claims = signedJWT.getJWTClaimsSet().getClaims();

            return new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);
        } catch (ParseException e) {
            throw new JwtException("Failed to parse JWT", e);
        }
    }
}
