package com.study.proudcat.domain.post.dto.request;

import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.user.entity.User;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WritePostReqeust {

    private String email;
    private String title;
    private String describe;

    @Builder
    public WritePostReqeust(String email, String title, String describe) {
        this.email = email;
        this.title = title;
        this.describe = describe;
    }

    public Post toEntity(User user) {
        return Post.builder()
                .title(title)
                .user(user)
                .describe(describe)
                .build();
    }
}
