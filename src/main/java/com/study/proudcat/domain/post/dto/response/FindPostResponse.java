package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.post.entity.Post;

import java.time.format.DateTimeFormatter;

public record FindPostResponse(Long id, String title, int heartCnt, int view, Long fileId,
                               String createdAt, String modifiedAt, String nickname, String email) {

    public static FindPostResponse from(Post post) {
        return new FindPostResponse(
                post.getId(),
                post.getTitle(),
                post.getHearts().size(),
                post.getView(),
                post.getFileId(),
                post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                post.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                post.getUser().getNickname(),
                post.getUser().getEmail()
        );
    }
}