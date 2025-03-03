package com.mdv.profile.mapper;

import org.mapstruct.Mapper;

import com.mdv.profile.dto.request.ProfileCreateRequest;
import com.mdv.profile.dto.response.UserProfileResponse;
import com.mdv.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toEntity(ProfileCreateRequest request);

    UserProfileResponse toResponse(UserProfile entity);
}
