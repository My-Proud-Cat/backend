package com.study.proudcat.domain.post.entity;

import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.infra.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String describe;

    @Column(nullable = false)
    private int view;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Post(User user, String title, String describe) {
        this.user = user;
        this.title = title;
        this.describe = describe;
        this.view = 0;
        this.status = Status.REGISTERED;
    }
}
