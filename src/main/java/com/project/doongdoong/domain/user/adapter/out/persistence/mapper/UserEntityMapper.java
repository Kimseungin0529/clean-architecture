package com.project.doongdoong.domain.user.adapter.out.persistence.mapper;

import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public UserEntity fromId(Long id) {
        return UserEntity.builder()
                .id(id)
                .build();
    }

    public UserEntity fromModel(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .socialId(user.getSocialId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .socialType(user.getSocialType())
                .emotionGrowth(user.getEmotionGrowth())
                .roles(user.getRoles())
                .build();
    }

    public User toModel(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .socialId(userEntity.getSocialId())
                .nickname(userEntity.getNickname())
                .email(userEntity.getEmail())
                .socialType(userEntity.getSocialType())
                .emotionGrowth(userEntity.getEmotionGrowth())
                .roles(userEntity.getRoles())
                .build();
    }
}
