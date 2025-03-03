package com.mdv.profile.service;

import org.springframework.stereotype.Service;

import com.mdv.profile.dto.request.ProfileCreateRequest;
import com.mdv.profile.dto.response.UserProfileResponse;
import com.mdv.profile.mapper.UserProfileMapper;
import com.mdv.profile.repository.UserProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserRepositoryService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreateRequest request) {
        log.info("Creating user profile for user: {}", request.getFirstName());
        return userProfileMapper.toResponse(userProfileRepository.save(userProfileMapper.toEntity(request)));
    }

    public UserProfileResponse getProfile(String id) {
        log.info("Fetching user profile for user: {}", id);
        return userProfileMapper.toResponse(userProfileRepository.findById(id).orElse(null));
    }
}
