package com.study.proudcat.domain.post.dto.response;

import com.study.proudcat.domain.comment.dto.CommentDetail;
import com.study.proudcat.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostDetail {

    private final Long postId;
    private final String title;
    private final String describe;
    private final Set<CommentDetail> commentDetails;
    private final int heartCnt;
    private final int view;

    public static PostDetail from(Post post) {
        return PostDetail.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .describe(post.getDescribe())
                .commentDetails(post.getComments()
                        .stream()
                        .map(CommentDetail::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .heartCnt(post.getHearts().size())
                .view(post.getView())
                .build();
    }
}
