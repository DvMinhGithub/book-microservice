package com.mdv.identity.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdv.identity.dto.request.UserCreateRequest;
import com.mdv.identity.dto.request.UserUpdateRequest;
import com.mdv.identity.dto.response.UserResponse;
import com.mdv.identity.entity.Role;
import com.mdv.identity.entity.User;
import com.mdv.identity.enums.UserRole;
import com.mdv.identity.exception.ApiErrorCode;
import com.mdv.identity.exception.ApiException;
import com.mdv.identity.mapper.ProfileMapper;
import com.mdv.identity.mapper.UserMapper;
import com.mdv.identity.repository.RoleRepository;
import com.mdv.identity.repository.UserRepository;
import com.mdv.identity.repository.htppClient.ProfileClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;

    private final PasswordEncoder passwordEncoder;

    private final ProfileClient profileClient;

    private static final String USER_NOT_FOUND = "User not found: ";

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User existUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (existUser != null) {
            throw new ApiException(ApiErrorCode.USER_EXISTED);
        }
        User user = userMapper.mapToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(UserRole.USER.toString()).ifPresent(roles::add);
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
            request.setId(user.getId());
            profileClient.createUser(profileMapper.mapToProfileCreateRequest(request));
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ApiErrorCode.USER_EXISTED);
        }

        return userMapper.mapToUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND + userId));
        return userMapper.mapToUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND + name));
        return userMapper.mapToUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException(USER_NOT_FOUND + userId));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
