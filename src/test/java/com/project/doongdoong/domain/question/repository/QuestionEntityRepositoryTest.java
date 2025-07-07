package com.project.doongdoong.domain.question.repository;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.question.adapter.out.persistence.QuestionJpaRepository;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.project.doongdoong.domain.question.domain.QuestionContent.*;

class QuestionEntityRepositoryTest extends IntegrationSupportTest {

    @Autowired
    QuestionJpaRepository questionRepository;
    @Autowired
    AnalysisJpaRepository analysisRepository;

    @DisplayName("해당하는 분석에 대한 모든 질문을 삭제한다.")
    @Test
    void deleteQuestionsById() {
        // given
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .build();

        QuestionEntity questionEntity1 = createQuestion(UNFIXED_QUESTION1);
        QuestionEntity questionEntity2 = createQuestion(UNFIXED_QUESTION3);
        QuestionEntity questionEntity3 = createQuestion(FIXED_QUESTION1);
        QuestionEntity questionEntity4 = createQuestion(UNFIXED_QUESTION4);

        questionEntity1.connectAnalysis(analysisEntity);
        questionEntity2.connectAnalysis(analysisEntity);
        questionEntity3.connectAnalysis(analysisEntity);
        questionEntity4.connectAnalysis(analysisEntity);

        analysisRepository.save(analysisEntity);
        questionRepository.saveAll(List.of(questionEntity1, questionEntity2, questionEntity3, questionEntity4));

        QuestionEntity questionEntity5 = createQuestion(FIXED_QUESTION4);
        questionRepository.save(questionEntity5);

        // when
        questionRepository.deleteQuestionsById(analysisEntity.getId());

        // then
        List<QuestionEntity> result = questionRepository.findAll();
        Assertions.assertThat(result).hasSize(1)
                .extracting("questionContent")
                .containsExactly(FIXED_QUESTION4);
    }

    private QuestionEntity createQuestion(QuestionContent questionContent) {
        return QuestionEntity.of(questionContent);
    }
}