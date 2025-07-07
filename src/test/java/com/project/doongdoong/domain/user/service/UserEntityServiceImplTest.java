package com.project.doongdoong.domain.user.service;

import com.project.doongdoong.domain.user.domain.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityServiceImplTest {

    @DisplayName("주어진 이메일과 사용자의 이메일이 같은지 비교합니다.")
    @Test
    void isSameEmail() {
        // given
        String email = "exam@test.com";
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .build();
        // when
        boolean result = userEntity.isSameEmail("exam@test.com");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 닉네임과 사용자의 닉네임이 같은지 비교합니다.")
    @Test
    void isSameNickname() {
        // given
        String nickname = "진이23";
        UserEntity userEntity = UserEntity.builder()
                .nickname(nickname)
                .build();
        // when
        boolean result = userEntity.isSameNickname("진이23");

        // then
        assertThat(result).isTrue();
    }


}