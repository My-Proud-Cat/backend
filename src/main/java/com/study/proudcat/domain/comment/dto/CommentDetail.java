package com.study.proudcat.domain.comment.dto;

import com.study.proudcat.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentDetail {

    private final Long id;
    private final String content;
    private final List<CommentDetail> childCommentDetailList;
    public static CommentDetail from(Comment comment) {
        return CommentDetail.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .childCommentDetailList(comment.getChildComments()
                        .stream()
                        .map(CommentDetail::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
