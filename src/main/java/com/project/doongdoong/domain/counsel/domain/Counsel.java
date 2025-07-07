package com.project.doongdoong.domain.counsel.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Counsel {
    private Long id;

    private String question;

    private String answer;

    private String imageUrl;

    private CounselType counselType;

    private Analysis analysis;

    private User user;

    @Builder
    public Counsel(String question, CounselType counselType, User user) {
        this.question = question;
        this.counselType = counselType;
        this.user = user;

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
