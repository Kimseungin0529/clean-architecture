package com.project.doongdoong.global.dto.response;

import lombok.*;

@Getter @Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    private TokenDto(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public static TokenDto of(String act, String rft){
        return TokenDto.builder()
                .accessToken(act)
                .refreshToken(rft)
                .build();
    }
}
