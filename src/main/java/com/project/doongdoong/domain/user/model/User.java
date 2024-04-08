package com.project.doongdoong.domain.user.model;

import com.project.doongdoong.domain.analysis.model.Analysis;
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
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String socailId;

    private String nickname;

    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "user_roles")
    private List<String> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analysis> analysisList = new ArrayList<>();

    // 권한 추가

    @Builder
    public User(String socailId, String nickname, String email, SocialType socialType) {
        this.socailId = socailId;
        this.nickname = nickname;
        this.email = email;
        this.socialType = socialType;
    }

    public void changeEmail(String email){
        this.email = email;
    }
    public void changeNickname(String nickname){
        this.nickname = nickname;
    }
}
