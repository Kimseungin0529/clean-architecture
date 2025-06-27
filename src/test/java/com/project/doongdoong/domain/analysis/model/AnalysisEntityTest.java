package com.project.doongdoong.domain.analysis.model;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.model.Answer;
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
        Answer answer1 = createAnswer("답변 내용1");
        Answer answer2 = createAnswer("답변 내용2");
        Answer answer3 = createAnswer("답변 내용3");
        Answer answer4 = createAnswer("답변 내용4");

        answer1.connectAnalysis(analysisEntity);
        answer2.connectAnalysis(analysisEntity);
        answer3.connectAnalysis(analysisEntity);
        answer4.connectAnalysis(analysisEntity);

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

    private Answer createAnswer(String content) {
        return Answer.builder()
                .content(content)
                .build();
    }

}