package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.comment.dto.CommentDetail;
import com.study.proudcat.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record PostDetails(
        Long postId, String title, String describe, Set<CommentDetail> commentDetails,
        int heartCnt, int view, LocalDateTime createdAt, LocalDateTime modifiedAt, Long fileId
) {
    public static PostDetails from(Post post) {
        return new PostDetails(
                post.getId(),
                post.getTitle(),
                post.getDescribe(),
                post.getComments()
                        .stream()
                        .map(CommentDetail::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                post.getHearts().size(),
                post.getView(),
                post.getCreatedAt(),
                post.getModifiedAt(),
                post.getFileId()
        );
    }
}
