package com.project.doongdoong.domain.counsel.exception;

import com.project.doongdoong.global.exception.CustomException;
import com.project.doongdoong.global.exception.ErrorType;

public class NOT_EXIST_PAGE_NUMBER extends CustomException.NotFoundException {
    public NOT_EXIST_PAGE_NUMBER() {
        super(ErrorType.NotFound.NOT_FOUND_DEFAULT, "존재하지 않는 페이지입니다.");
    }
}
