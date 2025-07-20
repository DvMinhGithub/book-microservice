package com.mdv.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mdv.post.dto.ApiResponse;
import com.mdv.post.dto.request.PostRequest;
import com.mdv.post.dto.response.PostResponse;
import com.mdv.post.service.PostService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostRequest request) {
        return ApiResponse.<PostResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.<List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(postService.getPosts())
                .build();
    }

}