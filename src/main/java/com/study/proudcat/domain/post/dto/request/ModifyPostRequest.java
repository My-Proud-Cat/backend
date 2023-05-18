package com.study.proudcat.domain.post.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyPostRequest {
    private String title;
    private String describe;

    @Builder
    public ModifyPostRequest( String title, String describe) {
        this.title = title;
        this.describe = describe;
    }
}
