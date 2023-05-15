package com.study.proudcat.domain.post.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

    REGISTERED("등록됨"),
    DELETED("삭제됨");

    private final String description;
}
