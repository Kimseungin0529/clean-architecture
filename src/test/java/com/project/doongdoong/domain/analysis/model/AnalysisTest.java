package com.project.doongdoong.domain.analysis.model;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.project.doongdoong.domain.question.domain.QuestionContent.*;
import static org.assertj.core.api.Assertions.assertThat;

class AnalysisTest {


    @DisplayName("감정 분석 날짜과 비교 날짜가 일치합니다.")
    @Test
    void equalsAnalyzeTimeTo_success() {
        // given
        LocalDate time = LocalDate.of(2024, 5, 10);
        LocalDate analyzeTime = LocalDate.of(2024, 5, 10);
        Analysis analysis = createAnalysis(analyzeTime);
        // when
        boolean result = analysis.equalsAnalyzeTimeTo(time);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("감정 분석 날짜과 비교 날짜가 일치하지 않습니다.")
    @Test
    void equalsAnalyzeTimeTo_fail() {
        // given
        LocalDate time = LocalDate.of(2024, 3, 24);
        LocalDate analyzeTime = LocalDate.of(2024, 5, 10);
        Analysis analysis = createAnalysis(analyzeTime);
        // when
        boolean result = analysis.equalsAnalyzeTimeTo(time);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("분석에 대한 감정 분석 시간이 존재합니다.")
    @Test
    void isAlreadyAnalyzed() {
        // given
        LocalDate analyzeTime = LocalDate.of(2024, 5, 10);
        Analysis analysis = createAnalysis(analyzeTime);

        // when
        boolean result = analysis.isAlreadyAnalyzed();

        // then
        assertThat(result).isTrue();
    }

    private Analysis createAnalysis(LocalDate analyzeDate) {
        Analysis analysisEntity = Analysis.builder()
                .build();
        analysisEntity.changeFeelingStateAndAnalyzeTime(0, analyzeDate);

        return analysisEntity;
    }

}