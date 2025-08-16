package com.project.doongdoong.domain.user.adapter.out.persistence.entity;

import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String socialId;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private long emotionGrowth;

    @ElementCollection(fetch = FetchType.EAGER) // security와 같이 사용할 권한 역할
    private List<String> roles = new ArrayList<>();

}
