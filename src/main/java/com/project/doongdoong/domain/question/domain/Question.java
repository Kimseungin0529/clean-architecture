package com.project.doongdoong.domain.question.domain;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.answer.domain.Answer;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Question {

    private Long id;

    private QuestionContent questionContent;

    private Answer answer;

    private Question(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public static Question of(QuestionContent questionContent) {
        return new Question(questionContent);
    }

    public static Question ofAll(Long id, QuestionContent questionContent, Answer answer) {
        Question question = Question.of(questionContent);
        question.id = id;
        question.answer = answer;
        return question;
    }


    public void connectAnswer(Answer Answer) {
        this.answer = Answer;
    }


    public boolean hasAnswer() {
        return answer != null;
    }
}
