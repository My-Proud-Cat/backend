package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostListResponse(Long id, String title, int heartCnt, int view,
                               LocalDateTime createdAt) {
    public static PostListResponse from(Post post) {
        return new PostListResponse(
                post.getId(),
                post.getTitle(),
                post.getHearts().size(),
                post.getView(),
                post.getCreatedAt()
        );
    }
}
