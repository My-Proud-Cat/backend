package com.study.proudcat.infra.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NO_TARGET(HttpStatus.NOT_FOUND, "해당되는 대상이 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입한 이메일 입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임 입니다."),
    NOT_VALID_TOKEN(HttpStatus.BAD_REQUEST, "Token이 유효하지 않습니다."),
    WRONG_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 확인하세요"),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "첨부된 파일이 없습니다."),
    NOT_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다. 이미지만 등록 가능합니다"),

    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "인증 정보가 만료되었습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 요청입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}

