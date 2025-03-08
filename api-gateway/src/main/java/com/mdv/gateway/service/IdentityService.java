package com.mdv.gateway.service;

import org.springframework.stereotype.Service;

import com.mdv.gateway.dto.request.IntrospectRequest;
import com.mdv.gateway.dto.response.ApiResponse;
import com.mdv.gateway.dto.response.IntrospecResponse;
import com.mdv.gateway.repository.IdentityClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityService {

    private final IdentityClient identityClient;
    
    public Mono<ApiResponse<IntrospecResponse>> introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder().token(token).build());
    }
}
