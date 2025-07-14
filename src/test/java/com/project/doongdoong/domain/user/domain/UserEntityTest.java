package com.project.doongdoong.domain.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @DisplayName("")
    @Test
    void fromModel() {
        // given
        User user = User.builder()
                .id(1L)
                .email("email@naver.com")
                .nickname("진이")
                .socialId("1235sa25")
                .socialType(SocialType.NAVER)
                .roles(List.of(Role.ROLE_USER.name()))
                .build();

        // when
        UserEntity result = UserEntity.fromModel(user);

        // then
        assertThat(result)
                .extracting(UserEntity::getId, UserEntity::getEmail, UserEntity::getNickname,
                        UserEntity::getSocialId, UserEntity::getSocialType, UserEntity::getRoles)
                .containsExactly(
                        1L, "email@naver.com", "진이", "1235sa25", SocialType.NAVER, List.of(Role.ROLE_USER.name())
                );

    }

}