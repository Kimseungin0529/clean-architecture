package com.project.doongdoong.domain.user.application.port.out;

import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;

import java.util.Optional;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId); // OAuth2 로그인 시, 사용하는 메소드

    Optional<UserEntity> findUserWithAnalysisBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
