package com.project.doongdoong.global.exception.servererror;

import com.project.doongdoong.global.exception.CustomException;
import com.project.doongdoong.global.exception.ErrorType;

import static com.project.doongdoong.global.exception.ErrorType.ServerError.EXTERNAL_SERVER_ERROR;

public class ExternalApiCallException extends CustomException.ServerErrorException {
    public ExternalApiCallException() {
        super(EXTERNAL_SERVER_ERROR, "외부 API 응답을 정상적으로 처리되지 않았습니다.");

    }
}
