package com.mdv.identity.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.mdv.identity.validator.DobConstraint;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotNull(message = "Username cannot be null")
    @Size(min = 5, message = "Username must be at least 5 characters long")
    String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(minAge = 18, message = "User must be at least 18 years old")
    LocalDate dob;

    List<String> roles;
}
