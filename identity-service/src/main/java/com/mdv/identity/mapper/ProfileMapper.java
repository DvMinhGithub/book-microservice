package com.mdv.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdv.identity.dto.request.ProfileCreateRequest;
import com.mdv.identity.dto.request.UserCreateRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "userId", source = "id")
    ProfileCreateRequest mapToProfileCreateRequest(UserCreateRequest request);
}
