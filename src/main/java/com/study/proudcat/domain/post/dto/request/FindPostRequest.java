package com.study.proudcat.domain.post.dto.request;

import com.study.proudcat.infra.utils.pagination.PageRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPostRequest extends PageRequest {

    private final String title;
    private final String searchCondition;

}
