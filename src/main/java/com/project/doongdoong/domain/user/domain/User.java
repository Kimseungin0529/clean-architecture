package com.project.doongdoong.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class User {

    private Long id;

    private String socialId;

    private String nickname;

    private String email;

    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private long emotionGrowth;

    private List<String> roles = new ArrayList<>();


    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void checkRoles() {
        this.roles = roles.isEmpty() ? Collections.singletonList(Role.ROLE_USER.toString()) : roles;

    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    public boolean isSameNickname(String nickname) {
        return this.nickname.equals(nickname);
    }


    public void growUp() {
        this.emotionGrowth++;
        checkGrowth();
    }

    private void checkGrowth() {
        if (getEmotionGrowth() == 101L)
            this.emotionGrowth %= 101;
    }
}
