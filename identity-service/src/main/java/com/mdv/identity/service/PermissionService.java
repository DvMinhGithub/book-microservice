package com.mdv.identity.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mdv.identity.dto.request.PermissionRequest;
import com.mdv.identity.dto.response.PermissionResponse;
import com.mdv.identity.entity.Permission;
import com.mdv.identity.mapper.PermissionMapper;
import com.mdv.identity.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.mapToPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.mapToPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permissionMapper::mapToPermissionResponse)
                .toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
