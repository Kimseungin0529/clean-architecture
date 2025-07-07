package com.project.doongdoong.domain.question.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Questions {
    private final List<QuestionEntity> questionEntities;

    private Questions(List<QuestionEntity> questionEntities) {
        this.questionEntities = questionEntities;
    }

    public static Questions from(List<QuestionEntity> questionEntities) {
        return new Questions(questionEntities);
    }


    public Questions extractRandomQuestions(int size) {
        if (isZeroBiggerThan(size)) {
            throw new IllegalArgumentException("크키는 최소 1 이상입니다.");
        }

        ArrayList<QuestionEntity> questionEntityList = new ArrayList<>(questionEntities);
        Collections.shuffle(questionEntityList);
        List<QuestionEntity> randomQuestionEntities = questionEntityList.subList(0, size);

        return from(randomQuestionEntities);
    }


    private boolean isZeroBiggerThan(int size) {
        return size <= 0;
    }

    public List<QuestionEntity> addQuestions(Questions newQuestions) {
        ArrayList<QuestionEntity> mergedQuestionEntities = new ArrayList<>(questionEntities);
        mergedQuestionEntities.addAll(newQuestions.questionEntities);

        return mergedQuestionEntities;
    }

}