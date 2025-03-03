package com.mdv.identity.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mdv.identity.dto.request.RoleRequest;
import com.mdv.identity.dto.response.RoleResponse;
import com.mdv.identity.entity.Permission;
import com.mdv.identity.entity.Role;
import com.mdv.identity.mapper.RoleMapper;
import com.mdv.identity.repository.PermissionRepository;
import com.mdv.identity.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.mapToRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        return roleMapper.mapToRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::mapToRoleResponse)
                .toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
