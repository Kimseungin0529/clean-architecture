package com.project.doongdoong.domain.user.application.port.out;

import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.domain.UserEntity;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId); // OAuth2 로그인 시, 사용하는 메소드

    Optional<User> findUserWithAnalysisBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
