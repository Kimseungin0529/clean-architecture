package com.project.doongdoong.domain.question.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {

    private Long id;

    private Long analysisId;

    private Long answerId;

    private final QuestionContent questionContent;

    @Builder
    public Question(Long id, QuestionContent questionContent, Long analysisId, Long answerId) {
        this.id = id;
        this.questionContent = questionContent;
        this.analysisId = analysisId;
        this.answerId = answerId;
    }

    private Question(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public static Question of(QuestionContent questionContent) {
        return new Question(questionContent);
    }

    public boolean isSame(Long id) {
        return this.id.equals(id);
    }

    public boolean hasAnswer() {
        return answerId != null;
    }

}
