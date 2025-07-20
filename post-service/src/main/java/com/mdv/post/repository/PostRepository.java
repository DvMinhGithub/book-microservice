package com.mdv.post.repository;

import com.mdv.post.entity.Post;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(String userId);
}
