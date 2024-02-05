package com.project.doongdoong.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter @Slf4j
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("KAKAO"), NAVER("NAVER"), GOOGLE("GOOGLE");

    private final String text;
    public static SocialType customValueOf(String socialType){
        SocialType type = null;
        log.info("socialType = {}", socialType);
        log.info("KAKAO = {}", KAKAO.getText());

        if(socialType.equals(KAKAO.getText())){
            type = KAKAO;
        }else if (socialType.equals(NAVER.getText())) {
            type = NAVER;
        }else if (socialType.equals(GOOGLE.getText())) {
            type = GOOGLE;
        }
        return type;
    }

}
