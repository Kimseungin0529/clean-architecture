package com.project.doongdoong.domain.questionanser.service;

import com.project.doongdoong.domain.questionanser.model.QuestionAnswer;

public interface QuestionAnswerService {

    QuestionAnswer createQuestionAnswer(String content);
    QuestionAnswer replyAnswer();
}
