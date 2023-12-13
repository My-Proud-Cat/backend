package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.comment.dto.CommentDetail;
import com.study.proudcat.domain.post.entity.Post;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record PostDetails(
        Long postId, String title, String describe, Set<CommentDetail> commentDetails,
        int heartCnt, int view, String createdAt, String modifiedAt, Long fileId, String nickname, String email
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
                post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                post.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                post.getFileId(),
                post.getUser().getNickname(),
                post.getUser().getEmail()
        );
    }
}
