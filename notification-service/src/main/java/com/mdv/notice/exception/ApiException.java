package com.mdv.notice.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ApiErrorCode apiErrorCode;

    public ApiException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode.getErrorMessage());
        this.apiErrorCode = apiErrorCode;
    }

    public ApiException(ApiErrorCode apiErrorCode, String message) {
        super(message);
        this.apiErrorCode = apiErrorCode;
    }

    public ApiException(ApiErrorCode apiErrorCode, String message, Throwable cause) {
        super(message, cause);
        this.apiErrorCode = apiErrorCode;
    }
} 