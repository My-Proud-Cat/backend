package com.study.proudcat.domain.comment.entity;

import com.study.proudcat.domain.comment.dto.CommentRequest;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;

    @Builder
    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    public static Comment of(CommentRequest request, Post post) {
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .build();
    }
}
