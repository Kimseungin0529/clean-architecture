package com.project.doongdoong.domain.question.service;

import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImp implements QuestionService {

    private QuestionRepository questionAnswerRepository;

    @Override
    public Question createQuestion(String content) {

        Question question = Question.builder()
                .question(content)
                .build();

        return null;
    }

}
