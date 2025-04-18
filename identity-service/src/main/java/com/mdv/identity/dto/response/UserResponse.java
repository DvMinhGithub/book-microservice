package com.mdv.identity.dto.response;

import java.util.Set;

import com.mdv.identity.entity.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    Set<Role> roles;
}
