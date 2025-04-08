package com.project.doongdoong.domain.user.service.spring;

import com.project.doongdoong.domain.user.service.UserService;
import com.project.doongdoong.global.dto.request.OAuthTokenDto;
import com.project.doongdoong.global.dto.response.TokenDto;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class UserServiceImplSpringTest extends IntegrationSupportTest {

    @Autowired
    UserService userService;


    @DisplayName("등록 여부에 따라 로그인 또는 회원가입 처리합니다.")
    @Test
    void checkRegistration() {

        // given
        String socialId = "123456789";
        String nickname = "testName";
        String email = "test@test.com";
        String socialType = "KAKAO";
        OAuthTokenDto request = new OAuthTokenDto(socialId, nickname, email, socialType);

        // when
        TokenDto result = userService.checkRegistration(request);

        // then
        assertThat(result)
                .extracting(TokenDto::getAccessToken, TokenDto::getRefreshToken)
                .allSatisfy(
                        token -> assertThat(token).isInstanceOf(String.class)
                );
    }


}