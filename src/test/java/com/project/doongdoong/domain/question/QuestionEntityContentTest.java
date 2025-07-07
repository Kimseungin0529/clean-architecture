package com.project.doongdoong.domain.question;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;


class QuestionEntityContentTest {
    @Test
    @DisplayName("고정 질문 항목 중 임의로 하나 반환한다.")
    void provideFixedQuestionContent(){
        //given
        List<QuestionContent> values = QuestionContent.getFixedQuestionContents();
        Random random = new Random();
        int index = random.nextInt(values.size());
        boolean isFixed = values.get(index).isFixedQuestion();

        //when
        QuestionContent fixedQuestionContent = QuestionContent.provideRandomFixedQuestionContent();

        //then
        assertThat(isFixed).isEqualTo(true);
        assertThat(values).contains(fixedQuestionContent);
    }

    @Test
    @DisplayName("비고정 질문 항목 중 임의로 하나 반환한다.")
    void provideUnFixedQuestionContent(){
        //give
        List<QuestionContent> values = QuestionContent.getUnFixedQuestionContents();
        Random random = new Random();
        int index = random.nextInt(values.size());
        boolean isFixed = values.get(index).isFixedQuestion();

        //when
        QuestionContent unFixedQuestionContent = QuestionContent.provideRandomUnFixedQuestionContent();

        //then
        assertThat(isFixed).isEqualTo(false);
        assertThat(values).contains(unFixedQuestionContent);
    }

}