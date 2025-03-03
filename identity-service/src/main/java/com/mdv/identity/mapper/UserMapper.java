package com.mdv.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.mdv.identity.dto.request.UserCreateRequest;
import com.mdv.identity.dto.request.UserUpdateRequest;
import com.mdv.identity.dto.response.UserResponse;
import com.mdv.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User mapToUser(UserCreateRequest request);

    UserResponse mapToUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
