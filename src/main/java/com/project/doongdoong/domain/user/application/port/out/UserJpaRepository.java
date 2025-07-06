package com.project.doongdoong.domain.user.application.port.out;

import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId); // OAuth2 로그인 시, 사용하는 메소드


    @Query("select u from UserEntity u left join u.analysisList al left join al.answers an " +
            "where u.socialType = :socialType and u.socialId = :socialId")
    Optional<UserEntity> findUserWithAnalysisBySocialTypeAndSocialId(@Param("socialType") SocialType socialType, @Param("socialId") String socialId);
}
