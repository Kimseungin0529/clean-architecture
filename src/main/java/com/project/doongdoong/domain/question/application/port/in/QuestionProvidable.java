package com.project.doongdoong.domain.question.application.port.in;

import com.project.doongdoong.domain.question.domain.Question;

import java.util.List;

public interface QuestionProvidable {

    Question createFixedQuestion();
    Question createUnFixedQuestion();

    List<Question> createRandomQuestions();
}
