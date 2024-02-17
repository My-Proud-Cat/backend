package com.study.proudcat.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "refresh")
    private String refresh;

    @Builder
    public RefreshToken(Long userId, String refresh) {
        this.userId = userId;
        this.refresh = refresh;
    }
}
