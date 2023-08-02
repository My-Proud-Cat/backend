package com.study.proudcat.domain.user.dto;

import com.study.proudcat.domain.user.entity.User;

public record UserResponse(Long userId, String email, String nickname) {

    public static UserResponse of(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname());
    }
}