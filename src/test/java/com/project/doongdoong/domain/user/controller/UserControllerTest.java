package com.project.doongdoong.domain.user.controller;

import com.project.doongdoong.global.dto.request.OAuthTokenDto;
import com.project.doongdoong.global.dto.response.TokenDto;
import com.project.doongdoong.module.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("사용자 로그인을 진행합니다. 회원 정보가 없다면 회원 정보 저장 후, 로그인 진행합니다.")
    @WithMockUser(username = "123456_APPLE")
    void userSignIn() throws Exception {
        // given
        String socialId = "123456";
        String nickname = "진이";
        String email = "test@naver.com";
        String socialType = "APPLE";
        OAuthTokenDto request = new OAuthTokenDto(socialId, nickname, email, socialType);

        String accessToken = "Bearer accessToken.xx.xx";
        String refreshToken = "Bearer refreshToken.xx.xx";
        TokenDto response = TokenDto.of(accessToken, refreshToken);

        given(userService.checkRegistration(any(OAuthTokenDto.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/login-oauth")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken));

    }

    @Test
    @DisplayName("사용자 로그인을 진행합니다. 회원 정보가 없다면 회원 정보 저장 후, 로그인 진행합니다.")
    @WithMockUser(username = "123456_APPLE")
    void userSignInExceptionOfValid() throws Exception {

        // given
        String socialId = "  ";
        String nickname = "    ";
        String email = "test24naver.com";
        String socialType = " ";
        OAuthTokenDto request = new OAuthTokenDto(socialId, nickname, email, socialType);

        String accessToken = "Bearer accessToken.xx.xx";
        String refreshToken = "Bearer refreshToken.xx.xx";
        TokenDto response = TokenDto.of(accessToken, refreshToken);

        given(userService.checkRegistration(any(OAuthTokenDto.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/login-oauth")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(0))
                .andExpect(jsonPath("$.message").value("잘못된 입력 형식이 존재합니다."))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details",
                        containsInAnyOrder("소셜 id이가 존재하지 않습니다.", "닉네임이 공백입니다.",
                                "이메일 형식이 아닙니다.", "OAuth 타입이 공백입니다.", "알파벳 대문자로 입력해주세요.")
                ));


    }

}