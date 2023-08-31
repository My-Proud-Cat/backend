package com.study.proudcat.domain.post.entity;

import com.study.proudcat.domain.comment.entity.Comment;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String describe;

    @Column(nullable = false)
    private int view;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post")
    @ToString.Exclude
    private Set<Heart> hearts;

    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Post(String title, String describe, Long fileId, User user, Set<Comment> comments, Set<Heart> hearts) {
        this.title = title;
        this.describe = describe;
        this.fileId = fileId;
        this.user = user;
        this.view = 0;
        this.status = Status.REGISTERED;
        this.comments = comments;
        this.hearts = hearts;
    }

    public void modify(String title, String describe) {
        this.title = title;
        this.describe = describe;
    }

    public void delete() {
        this.status = Status.DELETED;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
