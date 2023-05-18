package com.study.proudcat.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequest {

    private Long parentId;

    @NotBlank
    @Size(min = 10, message = "content는 2글자 이상 입력해야 합니다.")
    private String content;
}
