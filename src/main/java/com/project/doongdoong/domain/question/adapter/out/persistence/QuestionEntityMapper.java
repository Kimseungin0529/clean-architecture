package com.project.doongdoong.domain.question.adapter.out.persistence;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
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
