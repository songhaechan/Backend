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
    LOGIN_AGAIN("106", "다시 로그인해주세요."),

    // Member Error Code
    MEMBER_NOT_FOUND("201", "존재하지 않는 회원입니다."),
    DASH_NOT_ALLOWED("202", "전화번호에 '-'를 포함할 수 없습니다."),
    INVALID_GITHUB_URL("203", "부적절한 github 주소입니다."),

    // Group Error Code
    GROUP_NOT_FOUND("301", "존재하지 않는 그룹입니다."),
    START_TIME_AFTER_ENT_TIME("302", "시작시간이 종료시간보다 늦을 수 없습니다."),
    MAX_DURATION("303", "모임 지속 시간은 8시간을 넘을 수 없습니다."),
    MIN_DURATION("304", "모임 지속 시간은 1시간 이상 되어야 합니다."),
    NO_AUTHORITY_FOR_GROUP("305", "그룹에대한 권한이 없습니다."),
    VERIFICATION_FAILED("306", "모임 장소를 인증할 수 없습니다."),
    PARTICIPANT_NOT_FOUND("307", "모각코 참여자가 아닙니다."),
    LOCATION_NOT_FOUND("308", "주변 모각코가 존재하지 않습니다."),
    VERIFICATION_DUPLICATED("309", "이미 인증이 완료된 회원입니다."),
    PARTICIPANT_DUPLICATED("310", "중복된 회원입니다."),
    EXCEED_GROUP_CAPACITY("311", "그룹 수용 인원을 초과합니다.");

    private final String code;
    private final String message;
}
