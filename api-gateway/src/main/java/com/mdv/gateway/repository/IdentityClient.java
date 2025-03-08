package com.mdv.gateway.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.mdv.gateway.dto.request.IntrospectRequest;
import com.mdv.gateway.dto.response.ApiResponse;
import com.mdv.gateway.dto.response.IntrospecResponse;

import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospecResponse>> introspect(@RequestBody IntrospectRequest request);


}
