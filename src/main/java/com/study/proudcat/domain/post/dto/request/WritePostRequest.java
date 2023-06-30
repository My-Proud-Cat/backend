package com.study.proudcat.domain.post.dto.request;

import com.study.proudcat.domain.post.entity.Post;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WritePostRequest {
    private String title;
    private String describe;

    @Builder
    public WritePostRequest(String title, String describe) {
        this.title = title;
        this.describe = describe;
    }

    public Post toEntity(String fileName) {
        return Post.builder()
                .title(title)
                .describe(describe)
                .fileName(fileName)
                .build();
    }
}
