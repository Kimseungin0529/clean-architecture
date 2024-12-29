package com.project.doongdoong.domain.question.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.project.doongdoong.domain.question.model.QuestionContent.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionsTest {

    Questions questions;
    List<Question> questionList = List.of(
            Question.of(FIXED_QUESTION1),
            Question.of(FIXED_QUESTION5),
            Question.of(UNFIXED_QUESTION2),
            Question.of(UNFIXED_QUESTION4)
    );

    @BeforeEach
    void setUp() {
        questions = Questions.from(questionList);
    }

    @DisplayName("질문 리스트에서 임의로 지정한 크기만큼 질문을 반환한다.")
    @Test
    void extractRandomQuestions_success() {
        // given
        int size = 4;
        // when
        Questions result = questions.extractRandomQuestions(size);
        // then
        assertThat(result.getQuestions()).hasSize(size);
        assertThat(result.getQuestions())
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
        Question question1 = Question.of(FIXED_QUESTION2);
        Question question2 = Question.of(FIXED_QUESTION3);
        Question question3 = Question.of(UNFIXED_QUESTION1);
        Questions nextQuestions = Questions.from(List.of(question1, question2, question3));

        // when
        List<Question> result = questions.addQuestions(nextQuestions);

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