package com.project.doongdoong.domain.question.repository;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.project.doongdoong.domain.question.domain.QuestionContent.*;

class QuestionRepositoryTest extends IntegrationSupportTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnalysisJpaRepository analysisJpaRepository;

    @DisplayName("해당하는 분석에 대한 모든 질문을 삭제한다.")
    @Test
    void deleteQuestionsById() {
        // given
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .build();

        Question question1 = createQuestion(UNFIXED_QUESTION1);
        Question question2 = createQuestion(UNFIXED_QUESTION3);
        Question question3 = createQuestion(FIXED_QUESTION1);
        Question question4 = createQuestion(UNFIXED_QUESTION4);

        question1.connectAnalysis(analysisEntity);
        question2.connectAnalysis(analysisEntity);
        question3.connectAnalysis(analysisEntity);
        question4.connectAnalysis(analysisEntity);

        analysisJpaRepository.save(analysisEntity);
        questionRepository.saveAll(List.of(question1, question2, question3, question4));

        Question question5 = createQuestion(FIXED_QUESTION4);
        questionRepository.save(question5);

        // when
        questionRepository.deleteQuestionsById(analysisEntity.getId());

        // then
        List<Question> result = questionRepository.findAll();
        Assertions.assertThat(result).hasSize(1)
                .extracting("questionContent")
                .containsExactly(FIXED_QUESTION4);
    }

    private Question createQuestion(QuestionContent questionContent) {
        return Question.of(questionContent);
    }
}