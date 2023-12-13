package com.study.proudcat.domain.comment.dto;

import com.study.proudcat.domain.comment.entity.Comment;

import java.time.format.DateTimeFormatter;

public record CommentDetail(
        Long id, String content, String commentWriter, String createdAt, String email
) {
    public static CommentDetail from(Comment comment) {
        return new CommentDetail(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                comment.getUser().getEmail()
        );
    }
}
