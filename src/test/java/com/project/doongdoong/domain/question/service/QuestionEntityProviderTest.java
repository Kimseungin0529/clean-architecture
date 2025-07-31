package com.project.doongdoong.domain.question.service;

import com.project.doongdoong.domain.question.application.port.in.QuestionProvidable;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionEntityProviderTest extends IntegrationSupportTest {

    @Autowired
    QuestionProvidable questionProvider;

    @Test
    @DisplayName("임의로 고정 질문과 비고정 질문을 섞은 질문지를 제공한다.")
    void createRandomQuestions() {
        //given
        int expectedTotalSize = 4;
        //when
        List<Question> result = questionProvider.createRandomQuestions();
        //then
        assertThat(result)
                .hasSize(expectedTotalSize)
                .extracting(question -> question.getQuestionContent().isFixedQuestion())
                .containsExactly(true, true, false, false);

    }

    @DisplayName("임의의 고정 질문을 생성합니다.")
    @Test
    void createFixedQuestion() {
        // given & when
        Question result = questionProvider.createFixedQuestion();
        // then
        assertThat(result.getQuestionContent().isFixedQuestion()).isEqualTo(true);
    }

    @DisplayName("임의의 비고정 질문을 생성합니다.")
    @Test
    void createUnFixedQuestion() {
        // given & when
        Question result = questionProvider.createUnFixedQuestion();
        // then
        assertThat(result.getQuestionContent().isFixedQuestion()).isEqualTo(false);
    }


}