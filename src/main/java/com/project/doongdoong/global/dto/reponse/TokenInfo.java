package com.project.doongdoong.global.dto.reponse;

import lombok.*;

@Getter
public class TokenInfo {
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenInfo(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenInfo of(TokenInfo tokenInfoResponse){
        return TokenInfo.builder()
                .accessToken(tokenInfoResponse.getAccessToken())
                .refreshToken(tokenInfoResponse.getRefreshToken())
                .build();
    }
}
