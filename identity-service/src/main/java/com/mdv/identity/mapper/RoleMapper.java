package com.mdv.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mdv.identity.dto.request.RoleRequest;
import com.mdv.identity.dto.response.RoleResponse;
import com.mdv.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role mapToRole(RoleRequest request);

    RoleResponse mapToRoleResponse(Role role);
}
