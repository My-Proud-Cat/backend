package com.study.proudcat.domain.post.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyPostRequest {

    private String email;
    private String title;
    private String describe;

    @Builder
    public ModifyPostRequest(String email, String title, String describe) {
        this.email = email;
        this.title = title;
        this.describe = describe;
    }
}
