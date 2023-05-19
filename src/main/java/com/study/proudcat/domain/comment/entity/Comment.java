package com.study.proudcat.domain.comment.entity;

import com.study.proudcat.domain.comment.dto.CommentRequest;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "parent_id", updatable = false)
    private Long parentId;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Comment> childComments = new LinkedHashSet<>();

    public void setChildComments(Set<Comment> childComments) {
        this.childComments = childComments;
    }

    @Builder
    public Comment(Long parentId, String content, Post post, Set<Comment> childComments) {
        this.parentId = parentId;
        this.content = content;
        this.post = post;
        this.childComments = childComments;
    }

    public static Comment of(CommentRequest request, Post post) {
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .build();
    }

    public void addChildComment(Comment child) {
        child.setParentId(this.getId());
        this.getChildComments().add(child);
    }
}
