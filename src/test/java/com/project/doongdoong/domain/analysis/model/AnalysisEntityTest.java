package com.project.doongdoong.domain.analysis.model;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AnalysisEntityTest {


    @DisplayName("감정 분석 날짜과 비교 날짜가 일치합니다.")
    @Test
    void equalsAnalyzeTimeTo_success() {
        // given
        LocalDate time = LocalDate.of(2024, 5, 10);
        LocalDate analyzeTime = LocalDate.of(2024, 5, 10);
        AnalysisEntity analysisEntity = createAnalysis(analyzeTime);
        // when
        boolean result = analysisEntity.equalsAnalyzeTimeTo(time);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("감정 분석 날짜과 비교 날짜가 일치하지 않습니다.")
    @Test
    void equalsAnalyzeTimeTo_fail() {
        // given
        LocalDate time = LocalDate.of(2024, 3, 24);
        LocalDate analyzeTime = LocalDate.of(2024, 5, 10);
        AnalysisEntity analysisEntity = createAnalysis(analyzeTime);
        // when
        boolean result = analysisEntity.equalsAnalyzeTimeTo(time);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("분석에 대한 모든 답변을 했습니다.")
    @Test
    void hasAllAnswer() {
        // given
        AnalysisEntity analysisEntity = createAnalysis();
        AnswerEntity answerEntity1 = createAnswer("답변 내용1");
        AnswerEntity answerEntity2 = createAnswer("답변 내용2");
        AnswerEntity answerEntity3 = createAnswer("답변 내용3");
        AnswerEntity answerEntity4 = createAnswer("답변 내용4");

        answerEntity1.connectAnalysis(analysisEntity);
        answerEntity2.connectAnalysis(analysisEntity);
        answerEntity3.connectAnalysis(analysisEntity);
        answerEntity4.connectAnalysis(analysisEntity);

        // when
        boolean result = analysisEntity.hasAllAnswer();
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("분석에 대한 감정 분석 시간이 존재합니다.")
    @Test
    void isAlreadyAnalyzed() {
        // given
        LocalDate analyzeTime = LocalDate.of(2024, 5, 10);
        AnalysisEntity analysisEntity = createAnalysis(analyzeTime);

        // when
        boolean result = analysisEntity.isAlreadyAnalyzed();

        // then
        assertThat(result).isTrue();
    }

    private AnalysisEntity createAnalysis(LocalDate analyzeDate) {
        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .build();
        analysisEntity.changeFeelingStateAndAnalyzeTime(0, analyzeDate);

        return analysisEntity;
    }

    private AnalysisEntity createAnalysis() {

        return AnalysisEntity.builder()
                .build();
    }

    private AnswerEntity createAnswer(String content) {
        return AnswerEntity.builder()
                .content(content)
                .build();
    }

}