package com.project.doongdoong.domain.questionanser.service;

import com.project.doongdoong.domain.questionanser.model.QuestionAnswer;
import com.project.doongdoong.domain.questionanser.repository.QuestionAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionAnswerServiceImp implements QuestionAnswerService{

    private QuestionAnswerRepository questionAnswerRepository;

    @Override
    public QuestionAnswer createQuestionAnswer(String content) {

        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .question(content)
                .build();

        return null;
    }

    @Override
    public QuestionAnswer replyAnswer() {
        return null;
    }
}
