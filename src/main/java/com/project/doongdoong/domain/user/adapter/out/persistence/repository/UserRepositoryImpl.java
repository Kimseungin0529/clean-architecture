package com.project.doongdoong.domain.user.adapter.out.persistence.repository;

import com.project.doongdoong.domain.user.adapter.out.persistence.mapper.UserEntityMapper;
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

    private final UserEntityMapper userEntityMapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = userJpaRepository.save(userEntityMapper.fromModel(user));
        return userEntityMapper.toModel(userEntity);
    }

    @Override
    public Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return userJpaRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .map(userEntityMapper::toModel);
    }

}
