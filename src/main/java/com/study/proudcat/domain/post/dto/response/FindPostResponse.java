package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.post.entity.Post;

import java.time.LocalDateTime;

public record FindPostResponse(Long id, String title, String describe, String nickName, LocalDateTime createdAt,
                               LocalDateTime modifiedAt) {

    public static FindPostResponse from(Post post) {
        return new FindPostResponse(
                post.getId(),
                post.getTitle(),
                post.getDescribe(),
                post.getUser().getNickname(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
