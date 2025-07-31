package com.project.doongdoong.domain.user.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class User {

    private Long id;

    private String socialId;

    private String nickname;

    private String email;

    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private long emotionGrowth = 0L;

    private List<String> roles = new ArrayList<>();


    @Builder
    private User(String socialId, String nickname, String email, SocialType socialType) {
        this.socialId = socialId;
        this.nickname = nickname;
        this.email = email;
        this.socialType = socialType;
    }

    public static User of(String socialId, String nickname, String email, SocialType socialType){
        return new User(socialId, nickname, email, socialType);
    }

    public static User ofAll(Long id, String socialId, String nickname, String email
            , SocialType socialType, long emotionGrowth, List<String> roles) {

        User user = User.of(socialId, nickname, email, socialType);
        user.id = id;
        user.emotionGrowth = emotionGrowth;
        user.roles = roles;
        return user;
    }

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
