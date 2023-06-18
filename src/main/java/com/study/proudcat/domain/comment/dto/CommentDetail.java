package com.study.proudcat.domain.comment.dto;

import com.study.proudcat.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDetail {

    private final Long id;
    private final String content;
    public static CommentDetail from(Comment comment) {
        return CommentDetail.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
