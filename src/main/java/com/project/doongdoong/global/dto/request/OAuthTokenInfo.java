package com.project.doongdoong.global.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class OAuthTokenInfo {
    @NotBlank(message = "access_tokne이 존재하지 않습니다.")
    private String accessToken;

    @NotBlank(message = "refresh_tokne이 존재하지 않습니다.")
    private String refreshToken;

    @NotBlank(message = "닉네임이 공백입니다.")
    private String nickname;

    @NotBlank(message = "이메일이 공백입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "OAuth 타입이 공백입니다.")
    @Pattern(regexp = "[A-Z]+", message = "알파벳 대문자로 입력해주세요.")
    private String socailType;

}
