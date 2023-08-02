package com.study.proudcat.infra.security.jwt;

public record Subject(Long userId, String email, String nickname, String type) {

    public static Subject atk(Long userId, String email, String nickname) {
        return new Subject(userId, email, nickname, "ATK");
    }


    public static Subject rtk(Long userId, String email, String nickname) {
        return new Subject(userId, email, nickname, "RTK");
    }
}
