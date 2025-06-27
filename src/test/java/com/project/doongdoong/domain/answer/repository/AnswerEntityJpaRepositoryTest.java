package com.project.doongdoong.domain.answer.repository;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.answer.application.port.out.AnswerJpaRepository;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class AnswerEntityJpaRepositoryTest extends IntegrationSupportTest {

    @Autowired
    AnswerJpaRepository answerJpaRepository;
    @Autowired
    AnalysisJpaRepository analysisJpaRepository;

    @DisplayName("해당하는 분석에 대한 모든 답변을 삭제한다.")
    @Test
    void deleteAnswersById() {
        // given
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .build();

        AnswerEntity answerEntity1 = createAnswer("답변 1입니다.");
        AnswerEntity answerEntity2 = createAnswer("답변 2입니다.");
        AnswerEntity answerEntity3 = createAnswer("답변 3입니다.");
        AnswerEntity answerEntity4 = createAnswer("답변 4입니다.");

        answerEntity1.connectAnalysis(analysisEntity);
        answerEntity2.connectAnalysis(analysisEntity);
        answerEntity3.connectAnalysis(analysisEntity);
        answerEntity4.connectAnalysis(analysisEntity);

        analysisJpaRepository.save(analysisEntity);

        AnswerEntity answerEntity5 = createAnswer("답변 5입니다.");
        answerJpaRepository.save(answerEntity5);

        // when
        answerJpaRepository.deleteAnswersById(analysisEntity.getId());

        // then
        List<AnswerEntity> result = answerJpaRepository.findAll();
        Assertions.assertThat(result).hasSize(1)
                .extracting("content")
                .containsExactly("답변 5입니다.");
    }

    private AnswerEntity createAnswer(String content) {
        return AnswerEntity.builder()
                .content(content)
                .build();
    }

}