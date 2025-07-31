package com.project.doongdoong.domain.counsel.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Counsel {
    private Long id;

    private String question;

    private String answer;

    private String imageUrl;

    private CounselType counselType;

    private Analysis analysis;

    private User user;

    private LocalDateTime createdAt;

    @Builder
    private Counsel(String question, CounselType counselType, User user, LocalDateTime createdAt) {
        this.question = question;
        this.counselType = counselType;
        this.user = user;
        this.createdAt = createdAt;
    }

    public static Counsel of(String question, CounselType counselType, User user, LocalDateTime createdAt) {
        return Counsel.builder()
                .question(question)
                .counselType(counselType)
                .user(user)
                .createdAt(createdAt)
                .build();
    }

    public static Counsel ofAll(Long id, String question, CounselType counselType, User user, Analysis analysis, LocalDateTime createdAt) {
        Counsel counsel = Counsel.of(question, counselType, user, analysis, createdAt);
        counsel.id = id;
        return counsel;
    }

    public static Counsel of(String question, CounselType counselType, User user, Analysis analysis, LocalDateTime createdAt) {
        Counsel counsel = Counsel.of(question, counselType, user, createdAt);
        counsel.analysis = analysis;
        return counsel;
    }

    public void addAnalysis(Analysis analysis) {
        this.analysis = analysis;
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
