package com.project.doongdoong.domain.counsel.domain;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "counsel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounselEntity extends BaseEntity {

    @Id
    @Column(name = "counsel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000)
    private String question;

    @Column(length = 5000)
    private String answer;

    @Column(updatable = false, length = 1000)
    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private CounselType counselType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", unique = true, updatable = false)
    private AnalysisEntity analysis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private UserEntity user;

    @Builder
    public CounselEntity(String question, CounselType counselType, UserEntity userEntity) {
        this.question = question;
        this.counselType = counselType;
        this.user = userEntity;

    }

    public void addAnalysis(AnalysisEntity analysisEntity) {
        this.analysis = analysisEntity;
    }

    public void saveAnswer(String answer) {
        this.answer = answer;
    }

    public boolean hasAnalysis() {
        return this.getAnalysis() != null;
    }


    public void saveImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
