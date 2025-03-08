package com.mdv.profile.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mdv.profile.dto.response.UserProfileResponse;
import com.mdv.profile.service.UserRepositoryService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserRepositoryService userRepositoryService;

    @GetMapping("/users/{id}")
    public UserProfileResponse getProfile(@PathVariable("id") String id) {
        return userRepositoryService.getProfile(id);
    }

    @GetMapping("/users")
    public List<UserProfileResponse> getUsers() {
        return userRepositoryService.getUsers();
    }
}
