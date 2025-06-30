package com.project.doongdoong.domain.question.application.port.in;

import com.project.doongdoong.domain.question.domain.QuestionEntity;

import java.util.List;

public interface QuestionProvidable {

    QuestionEntity createFixedQuestion();
    QuestionEntity createUnFixedQuestion();

    List<QuestionEntity> createRandomQuestions();
}
