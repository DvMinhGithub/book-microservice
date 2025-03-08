package com.mdv.identity.repository.htppClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mdv.identity.configuration.AuthenticationRequestInterceptor;
import com.mdv.identity.dto.request.ProfileCreateRequest;
import com.mdv.identity.dto.response.UserProfileResponse;

@FeignClient(
        name = "profile-service",
        url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {

    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createUser(@RequestBody ProfileCreateRequest request);
}
