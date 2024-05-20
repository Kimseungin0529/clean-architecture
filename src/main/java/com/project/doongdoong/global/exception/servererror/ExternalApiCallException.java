package com.project.doongdoong.global.exception.servererror;

import com.project.doongdoong.global.exception.CustomException;
import com.project.doongdoong.global.exception.ErrorType;

import static com.project.doongdoong.global.exception.ErrorType.ServerError.EXTERNAL_SERVER_ERROR;

public class ExternalApiCallException extends CustomException.ServerErrorException {
    public ExternalApiCallException() {
        super(EXTERNAL_SERVER_ERROR, "외부 API 호출을 실패했습니다.");

    }
}
