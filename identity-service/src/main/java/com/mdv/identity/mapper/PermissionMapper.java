package com.mdv.identity.mapper;

import org.mapstruct.Mapper;

import com.mdv.identity.dto.request.PermissionRequest;
import com.mdv.identity.dto.response.PermissionResponse;
import com.mdv.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission mapToPermission(PermissionRequest request);

    PermissionResponse mapToPermissionResponse(Permission permission);
}
