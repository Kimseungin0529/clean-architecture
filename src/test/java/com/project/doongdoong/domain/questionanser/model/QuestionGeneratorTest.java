package com.project.doongdoong.domain.questionanser.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;


class QuestionGeneratorTest {

    @Test
    @DisplayName("QuestionContent은 각 타입별 지정된 개수의 질문 리스트를 반환한다.")
    void provideQuestions1(){
        //given
        int totalQuestionSize = 4;
        //when
        List<QuestionContent> result = QuestionContent.provideQuestions();
        //then
        Assertions.assertThat(result).hasSize(totalQuestionSize);

    }

    @Test
    @DisplayName("QuestionContent은 각각 지정된 개수의 타입 별 질문 리스트를 반환한다. ")
    void provideQuestions2(){
        //given
        int totalQuestionSize = 4;
        int fixedQuestion = 2;
        int unFixedQuestion = 2;
        List<QuestionContent> result = QuestionContent.provideQuestions();
        //when
        int fixedSize = result.stream().filter(Question -> Question.isFixedQuestion()).collect(Collectors.toList()).size();
        int unFixedSize = result.stream().filter(Question -> !Question.isFixedQuestion()).collect(Collectors.toList()).size();
        //then
        Assertions.assertThat(fixedSize).isEqualTo(fixedQuestion);
        Assertions.assertThat(unFixedSize).isEqualTo(unFixedQuestion);
    }

}