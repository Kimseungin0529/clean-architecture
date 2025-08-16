package com.project.doongdoong.domain.question.application.port.dto;

import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.domain.Question;
import lombok.Getter;

@Getter
public class QuestionAnswer {
    private final Question question;
    private final Answer answer;

    public static QuestionAnswer of(Question question, Answer answer) {
        return new QuestionAnswer(question, answer);
    }
    private QuestionAnswer(Question question, Answer answer) {
        this.question = question;
        this.answer = answer;
    }
}
