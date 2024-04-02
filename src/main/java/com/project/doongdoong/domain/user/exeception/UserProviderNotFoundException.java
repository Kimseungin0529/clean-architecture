package com.project.doongdoong.domain.user.exeception;

import com.project.doongdoong.global.exception.CustomException;
import com.project.doongdoong.global.exception.ErrorType;

import static com.project.doongdoong.global.exception.ErrorType.NotFound.USER_PROVIDER_NOT_FOUND;

public class UserProviderNotFoundException extends CustomException.NotFoundException {
    public UserProviderNotFoundException() {
        super(USER_PROVIDER_NOT_FOUND, "해당 소셜 타입은 존재하지 않습니다. 대문자로 입력해주세요.");
    }
}
