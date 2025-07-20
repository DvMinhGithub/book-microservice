package com.mdv.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mdv.post.dto.request.PostRequest;
import com.mdv.post.dto.response.PostResponse;

@Service
public interface PostService {
    PostResponse createPost(PostRequest request);

    List<PostResponse> getPosts();
}
