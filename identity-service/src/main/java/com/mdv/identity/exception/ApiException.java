package com.mdv.identity.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ApiErrorCode apiErrorCode;

    public ApiException(ApiErrorCode apiErrorCode) {
        super(apiErrorCode.getErrorMessage());
        this.apiErrorCode = apiErrorCode;
    }
}
