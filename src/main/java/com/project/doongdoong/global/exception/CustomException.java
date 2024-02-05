package com.project.doongdoong.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    int errorCode; // http 번호
    String codeMassage; // http 간편 설명
    String detail; // 자세한 예외 메세지

    public CustomException(HttpStatus status, String detail){
        this.errorCode = status.value();
        this.codeMassage = status.getReasonPhrase();
        this.detail = detail;
    }

    public static class InvalidRequestException extends CustomException {
        public InvalidRequestException(HttpStatus status, String detail) {
            super(status, detail);
        }
    }

    public static class UnauthorizedException extends CustomException {
        public UnauthorizedException(HttpStatus status, String detail) {
            super(status, detail);
        }
    }

    public static class ForbiddenException extends CustomException {
        public ForbiddenException(HttpStatus status, String detail) {
            super(status, detail);
        }
    }

    public static class NotFoundException extends CustomException {
        public NotFoundException(HttpStatus status, String detail) {
            super(status, detail);
        }
    }

    public static class ConflictException extends CustomException {
        public ConflictException(HttpStatus status, String detail) {
            super(status, detail);
        }
    }

    public static class ServerErrorException extends CustomException {
        public ServerErrorException(HttpStatus status, String detail) {
            super(status, detail);
        }
    }
}
