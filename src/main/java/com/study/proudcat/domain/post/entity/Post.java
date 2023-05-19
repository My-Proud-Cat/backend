package com.study.proudcat.domain.post.entity;

import com.study.proudcat.domain.comment.entity.Comment;
import com.study.proudcat.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String describe;

    @Column(nullable = false)
    private int view;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Builder
    public Post(String title, String describe) {
        this.title = title;
        this.describe = describe;
        this.view = 0;
        this.status = Status.REGISTERED;
    }

    public void modify(String title, String describe) {
        this.title = title;
        this.describe = describe;
    }

    public void delete() {
        this.status = Status.DELETED;
    }
}
