package com.project.doongdoong.domain.counsel.domain;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "counsel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CounselEntity extends BaseEntity {

    @Id
    @Column(name = "counsel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private AnalysisEntity analysis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 5000)
    private String question;

    @Column(length = 5000)
    private String answer;

    @Column(updatable = false, length = 1000)
    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private CounselType counselType;

    @Column(updatable = false)
    private LocalDateTime createdAt;

}
