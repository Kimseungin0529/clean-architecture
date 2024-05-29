package com.project.doongdoong.domain.counsel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CounselDetailResponse {

    private Long counselId;
    private String question;
    private String answer;
    private String counselType;
    private List<String> analysisQuestions;
    private List<String> analysisAnswers;

    @Builder
    public CounselDetailResponse(Long counselId, String answer, String counselType) {
        this.counselId = counselId;
        this.answer = answer;
        this.counselType = counselType;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnalysisQuestions(List<String> analysisQuestions) {
        this.analysisQuestions = analysisQuestions;
    }

    public void setAnalysisAnswers(List<String> analysisAnswers) {
        this.analysisAnswers = analysisAnswers;
    }
}
