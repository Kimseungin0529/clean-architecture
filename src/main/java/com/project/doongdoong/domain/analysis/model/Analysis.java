package com.project.doongdoong.domain.analysis.model;

import com.project.doongdoong.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter                                            // pubilc 대신 protected? 무분별한 생성을 막기 위해서 라는데 무분별하게 생성할 일이 있을까?
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 지연 로딩으로 인해 프록시 객체(상속)하므로 private 대신 protected를 사용
public class Analysis {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "analsis_id")
    private Long id;

    private long feelingState;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
