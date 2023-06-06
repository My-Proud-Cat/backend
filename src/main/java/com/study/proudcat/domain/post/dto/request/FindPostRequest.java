package com.study.proudcat.domain.post.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPostRequest {

    private final String title;
    private final String searchCondition;

}
