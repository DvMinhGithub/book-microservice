package com.mdv.profile.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdv.profile.dto.request.ProfileCreateRequest;
import com.mdv.profile.dto.response.UserProfileResponse;
import com.mdv.profile.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(target = "id", ignore = true)
    UserProfile toEntity(ProfileCreateRequest request);

    UserProfileResponse toResponse(UserProfile entity);
}
