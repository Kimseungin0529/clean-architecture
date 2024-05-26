package com.project.doongdoong.domain.counsel.exception;

import com.project.doongdoong.global.exception.CustomException;
import com.project.doongdoong.global.exception.ErrorType;

public class CounselTypeInvalidException extends CustomException.InvalidRequestException {
    public CounselTypeInvalidException(String detail) {
        super(ErrorType.BadRequest.COUNSEL_TYPE_WRONG, detail + "라는 타입은 없습니다.");
    }
}
