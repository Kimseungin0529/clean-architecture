package com.project.doongdoong.domain.question.model;

import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.question.domain.Questions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.project.doongdoong.domain.question.domain.QuestionContent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionsTest {

    Questions questions;
    List<QuestionEntity> questionEntityList = List.of(
            QuestionEntity.of(FIXED_QUESTION1),
            QuestionEntity.of(FIXED_QUESTION5),
            QuestionEntity.of(UNFIXED_QUESTION2),
            QuestionEntity.of(UNFIXED_QUESTION4)
    );

    @BeforeEach
    void setUp() {
        questions = Questions.from(questionEntityList);
    }

    @DisplayName("질문 리스트에서 임의로 지정한 크기만큼 질문을 반환한다.")
    @Test
    void extractRandomQuestions_success() {
        // given
        int size = 4;
        // when
        Questions result = questions.extractRandomQuestions(size);
        // then
        assertThat(result.getQuestionEntities()).hasSize(size);
        assertThat(result.getQuestionEntities())
                .extracting("questionContent")
                .containsExactlyInAnyOrder(
                        FIXED_QUESTION1,
                        FIXED_QUESTION5,
                        UNFIXED_QUESTION2,
                        UNFIXED_QUESTION4
                );
    }

    @DisplayName("최소 1개 이상부터 질문을 반환할 수 있다.")
    @Test
    void extractRandomQuestions_fail() {
        // given
        int size = 0;
        // when & then
        assertThatThrownBy(() -> questions.extractRandomQuestions(size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("크키는 최소 1 이상입니다.");
    }


    @DisplayName("기존 질문 목록에 추가 질문 목록을 뒤에 이어 반환합니다.")
    @Test
    void createRandomQuestions() {
        // given
        QuestionEntity questionEntity1 = QuestionEntity.of(FIXED_QUESTION2);
        QuestionEntity questionEntity2 = QuestionEntity.of(FIXED_QUESTION3);
        QuestionEntity questionEntity3 = QuestionEntity.of(UNFIXED_QUESTION1);
        Questions nextQuestions = Questions.from(List.of(questionEntity1, questionEntity2, questionEntity3));

        // when
        List<QuestionEntity> result = questions.addQuestions(nextQuestions);

        // then
        assertThat(result)
                .hasSize(7)
                .extracting("questionContent")
                .containsExactly(
                        FIXED_QUESTION1,
                        FIXED_QUESTION5,
                        UNFIXED_QUESTION2,
                        UNFIXED_QUESTION4,
                        FIXED_QUESTION2,
                        FIXED_QUESTION3,
                        UNFIXED_QUESTION1
                );
    }



}