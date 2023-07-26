package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.post.entity.Post;

import java.time.LocalDateTime;

public record FindPostResponse(Long id, String title, int heartCnt, int view, byte[] fileData,
                               LocalDateTime createdAt, LocalDateTime modifiedAt) {

    public static FindPostResponse from(Post post, byte[] byteFile) {
        return new FindPostResponse(
                post.getId(),
                post.getTitle(),
                post.getHearts().size(),
                post.getView(),
                byteFile,
                post.getCreatedAt(),
                post.getModifiedAt()
        );
    }
}
