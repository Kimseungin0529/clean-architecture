package com.project.doongdoong.domain.user.model;

import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String nickname;

    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "user_roles")
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @Builder
    public User(String nickname, String email, SocialType socialType) {
        this.nickname = nickname;
        this.email = email;
        this.socialType = socialType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    // private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)
    // 앱에서 SDK 인증으로 이미 검증된 상태로 소셜 token값만 받음. OAuth 라이브러리를 사용하지 못해 직접 id 값을 꺼낼 수 가 없음.
    // 보안 상, 매우 부족해 보이지만 일단 이메일과 socialType으로만 사용자 검증하자.

}
