package com.project.doongdoong.domain.user.adapter.out.persistence;

import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity save(UserEntity userEntity) {
        return userJpaRepository.save(userEntity);
    }

    @Override
    public Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return userJpaRepository.findBySocialTypeAndSocialId(socialType, socialId);
    }

    @Override
    public Optional<UserEntity> findUserWithAnalysisBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return userJpaRepository.findUserWithAnalysisBySocialTypeAndSocialId(socialType, socialId);
    }
}
