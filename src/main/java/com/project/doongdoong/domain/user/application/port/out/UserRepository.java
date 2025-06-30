package com.project.doongdoong.domain.user.application.port.out;

import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId); // OAuth2 로그인 시, 사용하는 메소드


    @Query("select u from User u left join u.analysisList al left join al.answerEntities an " +
            "where u.socialType = :socialType and u.socialId = :socialId")
    Optional<User> findUserWithAnalysisBySocialTypeAndSocialId(@Param("socialType") SocialType socialType, @Param("socialId") String socialId);
}
