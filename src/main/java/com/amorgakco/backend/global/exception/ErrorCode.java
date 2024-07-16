package com.amorgakco.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // Auth Error Code
    ACCESS_TOKEN_EXPIRED("100", "액세스 토큰이 만료됐습니다."),
    REFRESH_TOKEN_REQUIRED("101", "리프레쉬 토큰을 쿠키에서 찾을 수 없습니다."),
    TOKEN_CLAIM_NOT_MATCHED("102", "액세스 토큰과 리프레쉬 토큰의 정보가 다릅니다."),
    CANNOT_PARSE_TOKEN("103", "식별할 수 없는 액세스토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND("104", "리프레쉬 토큰을 찾을 수 없습니다."),
    ACCESS_TOKEN_NOT_FOUND("105", "헤더에서 액세스 토큰을 찾을 수 없습니다."),
    RECHECK_YOUR_TOKEN("106", "액세스 토큰과 리프레쉬 토큰을 다시 확인바랍니다."),

    // Member Error Code
    MEMBER_NOT_FOUND("201", "존재하지 않는 회원입니다."),

    // Group Error Code
    GROUP_NOT_FOUND("301", "존재하지 않는 그룹입니다.");

    private final String code;
    private final String message;
}
