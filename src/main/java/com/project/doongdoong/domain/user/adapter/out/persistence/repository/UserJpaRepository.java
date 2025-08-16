package com.project.doongdoong.domain.user.adapter.out.persistence.repository;

import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId); // OAuth2 로그인 시, 사용하는 메소드

}
