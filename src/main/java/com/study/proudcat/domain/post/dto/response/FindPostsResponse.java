package com.study.proudcat.domain.post.dto.response;

import java.util.List;

public record FindPostsResponse(List<FindPostResponse> posts) {
}
