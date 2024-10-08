package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IllegalAccessException extends IllegalStateException {
    private final ErrorCode errorCode;

    public static IllegalAccessException refreshTokenRequired() {
        return new IllegalAccessException(ErrorCode.REFRESH_TOKEN_REQUIRED);
    }

    public static IllegalAccessException noAuthorityForGroup() {
        return new IllegalAccessException(ErrorCode.NO_AUTHORITY_FOR_GROUP);
    }

    public static IllegalAccessException verificationFailed() {
        return new IllegalAccessException(ErrorCode.VERIFICATION_FAILED);
    }

    public static IllegalAccessException verificationDuplicated() {
        return new IllegalAccessException(ErrorCode.VERIFICATION_DUPLICATED);
    }

    public static IllegalAccessException duplicatedParticipant() {
        return new IllegalAccessException(ErrorCode.PARTICIPANT_DUPLICATED);
    }

    public static IllegalAccessException exceedGroupCapacity() {
        return new IllegalAccessException(ErrorCode.PARTICIPANT_DUPLICATED);
    }
}
