package com.project.doongdoong.domain.question.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.answer.domain.Answer;
import lombok.Getter;

@Getter
public class Question {

    private Long id;


    private QuestionContent questionContent;

    private Analysis analysis;

    private Answer answer;

    private Question(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public static Question of(QuestionContent questionContent) {
        return new Question(questionContent);
    }

    public void connectAnalysis(Analysis analysis) {
        if (this.analysis != null) {
            return;
        }
        this.analysis = analysis; // 양방향 연관관계 메서드를 맺지 않아도 되는 이유 -> 생성자에서 이미 questions 객체를 넣어줌. 이미 완료됨.
    }

    public void connectAnswer(Answer Answer) {
        this.answer = Answer;
    }


    public boolean hasAnswer() {
        return answer != null;
    }
}
