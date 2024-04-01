package com.project.doongdoong.domain.question.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;


class QuestionGeneratorTest {
    @Test
    @DisplayName("고정 QuestionContent을 랜덤으로 하나 반환한다. ")
    void provideFixedQuestionContent(){
        List<QuestionContent> values = List.of(QuestionContent.FIXED_QUESTION1
                , QuestionContent.FIXED_QUESTION2
                , QuestionContent.FIXED_QUESTION3
                , QuestionContent.FIXED_QUESTION4
        );
        boolean isFixed = QuestionContent.FIXED_QUESTION1.isFixedQuestion();

        QuestionContent fixedQuestionContent = QuestionContent.provideFixedQuestionContent();

        Assertions.assertThat(isFixed).isEqualTo(QuestionContent.FIXED_QUESTION2.isFixedQuestion());
        Assertions.assertThat(values).contains(fixedQuestionContent);

    }

    @Test
    @DisplayName("고정되지 않은 QuestionContent을 랜덤으로 하나 반환한다. ")
    void provideUnFixedQuestionContent(){
        List<QuestionContent> values = List.of(QuestionContent.UNFIXED_QUESTION1
                , QuestionContent.UNFIXED_QUESTION2
                , QuestionContent.UNFIXED_QUESTION3
                , QuestionContent.UNFIXED_QUESTION4
        );

        boolean isUnFixed = QuestionContent.UNFIXED_QUESTION1.isFixedQuestion();

        QuestionContent unFixedQuestionContent = QuestionContent.provideUnFixedQuestionContent();

        Assertions.assertThat(isUnFixed).isEqualTo(QuestionContent.UNFIXED_QUESTION2.isFixedQuestion());
        Assertions.assertThat(values).contains(unFixedQuestionContent);
    }

    /*@Test
    @DisplayName("QuestionContent은 각 타입별 지정된 개수의 질문 리스트를 반환한다.")
    void provideQuestions1(){
        //given
        int totalQuestionSize = 4;
        //when
        *//*List<QuestionContent> result = QuestionContent.provideQuestions();
        //then
        Assertions.assertThat(result).hasSize(totalQuestionSize);*//*

    }

    @Test
    @DisplayName("QuestionContent은 각각 지정된 개수의 타입 별 질문 리스트를 반환한다. ")
    void provideQuestions2(){
        //given
        int totalQuestionSize = 4;
        int fixedQuestion = 2;
        int unFixedQuestion = 2;
    *//*    List<QuestionContent> result = QuestionContent.provideQuestions();
        //when
        int fixedSize = result.stream().filter(Question -> Question.isFixedQuestion()).collect(Collectors.toList()).size();
        int unFixedSize = result.stream().filter(Question -> !Question.isFixedQuestion()).collect(Collectors.toList()).size();
        //then
        Assertions.assertThat(fixedSize).isEqualTo(fixedQuestion);
        Assertions.assertThat(unFixedSize).isEqualTo(unFixedQuestion);*//*
        Assertions.assertThat(0).isEqualTo(0);
    }*/

}