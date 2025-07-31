package com.project.doongdoong.domain.user.adapter.out.persistence;

import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }

    @Override
    public Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return userJpaRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findUserWithAnalysisBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return userJpaRepository.findUserWithAnalysisBySocialTypeAndSocialId(socialType, socialId)
                .map(UserEntity::toModel);
    }
}
