package com.project.doongdoong.global.exception;


public interface ErrorType {
    int getCode();

    enum BadRequest implements ErrorType {
        BAD_REQUEST_DEFAULT(0),
        FILE_EMPTY(1);

        private final int errorCode;

        BadRequest(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum Unauthorized implements ErrorType {
        UNAUTHORIZED_DEFAULT(1000),
        LOGIN_FAILED(1001)
        ;

        private final int errorCode;

        Unauthorized(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum Forbidden implements ErrorType {
        FORBIDDEN_DEFAULT(3000),
        TOKEN_INFO_FORBIDDEN(3001),
        ACCESS_DENIED(3100)
        ;

        private final int errorCode;

        Forbidden(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum NotFound implements ErrorType {
        NOT_FOUND_DEFAULT(4000),
        USER_PROVIDER_NOT_FOUND(4001),
        REFRESH_TOKEN_NOT_FOUND(4002),
        IMAGE_URL_NOT_FOUND(4003),
        VOICE_URL_NOT_FOUND(4004)
        ;

        private final int errorCode;

        NotFound(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum Conflict implements ErrorType {
        CONFLICT_DEFAULT(9000)
        ;

        private final int errorCode;

        Conflict(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }

    enum ServerError implements ErrorType {
        SERVER_ERROR_DEFAULT(5000),
        FILE_UPLOAD_FAIL(5001),
        FILE_DELETE_FAIL(5002)
        ;

        private final int errorCode;

        ServerError(int errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public int getCode() {
            return this.errorCode;
        }
    }
}

