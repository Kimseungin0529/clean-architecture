package com.project.doongdoong.domain.counsel.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class Counsel {
    private Long id;

    private Long userId;

    private Long analysisId;

    private String question;

    private String answer;

    private String imageUrl;

    private CounselType counselType;

    private LocalDateTime createdAt;

    public static Counsel of(String question, CounselType counselType, Long userId, LocalDateTime createdAt) {
        return Counsel.builder()
                .question(question)
                .counselType(counselType)
                .userId(userId)
                .createdAt(createdAt)
                .build();
    }

    public static Counsel of(String question, CounselType counselType, Long userId, Long analysisId, LocalDateTime createdAt) {
        Counsel counsel = Counsel.of(question, counselType, userId, createdAt);
        counsel.analysisId = analysisId;
        return counsel;
    }

    public void addAnalysisId(Long analysisId) {
        this.analysisId = analysisId;
    }

    public void saveAnswer(String answer) {
        this.answer = answer;
    }

    public boolean hasAnalysis() {
        return this.analysisId != null;
    }

    public void saveImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
