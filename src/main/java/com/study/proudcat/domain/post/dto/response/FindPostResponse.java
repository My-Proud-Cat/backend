package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.post.entity.Post;

import java.time.LocalDateTime;

public record FindPostResponse(Long id, String title, String describe, int heartCnt, int view,
                               LocalDateTime createdAt, LocalDateTime modifiedAt) {

    public static FindPostResponse from(Post post) {
        return new FindPostResponse(
                post.getId(),
                post.getTitle(),
                post.getDescribe(),
                post.getHearts().size(),
                post.getView(),
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
