package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.post.entity.Post;

import java.time.LocalDateTime;

public record FindPostResponse(Long id, String title, int heartCnt, int view, Long fileId,
                               LocalDateTime createdAt, LocalDateTime modifiedAt, String nickname) {

    public static FindPostResponse from(Post post) {
        return new FindPostResponse(
                post.getId(),
                post.getTitle(),
                post.getHearts().size(),
                post.getView(),
                post.getFileId(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                post.getUser().getNickname()
        );
    }
}
