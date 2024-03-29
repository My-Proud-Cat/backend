package com.study.proudcat.domain.post.dto.request;

import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.user.entity.User;
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

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .describe(describe)
                .user(user)
                .build();
    }
}
