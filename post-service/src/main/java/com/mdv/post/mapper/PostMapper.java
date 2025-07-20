package com.mdv.post.mapper;

import org.mapstruct.Mapper;

import com.mdv.post.dto.response.PostResponse;
import com.mdv.post.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
}
