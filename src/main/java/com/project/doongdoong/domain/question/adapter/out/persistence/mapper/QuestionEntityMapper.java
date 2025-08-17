package com.project.doongdoong.domain.question.adapter.out.persistence.mapper;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.entitiy.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.adapter.out.persistence.entity.AnswerEntity;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.adapter.out.persistence.entity.QuestionEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestionEntityMapper {

    public QuestionEntity fromModel(Question question, AnalysisEntity analysisEntity, AnswerEntity answerEntity) {

        return QuestionEntity.builder()
                .id(question.getId())
                .questionContent(question.getQuestionContent())
                .analysis(analysisEntity)
                .answer(answerEntity)
                .build();
    }

    public Question toModel(QuestionEntity questionEntity) {

        return Question.builder()
                .id(questionEntity.getId())
                .questionContent(questionEntity.getQuestionContent())
                .build();
    }


    public Question toModel(QuestionEntity questionEntity, Answer answer) {

        return Question.builder()
                .id(questionEntity.getId())
                .answerId(answer.getId())
                .questionContent(questionEntity.getQuestionContent())
                .build();
    }
}
