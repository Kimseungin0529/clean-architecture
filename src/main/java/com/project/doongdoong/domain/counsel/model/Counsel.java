package com.project.doongdoong.domain.counsel.model;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counsel extends BaseEntity {

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
    private User user;

    @Builder
    public Counsel(String question, CounselType counselType, User user) {
        this.question = question;
        this.counselType = counselType;
        this.user = user;

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
