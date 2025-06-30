package com.project.doongdoong.domain.question.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Questions {
    private final List<Question> questions;

    private Questions(List<Question> questions) {
        this.questions = questions;
    }

    public static Questions from(List<Question> questions) {
        return new Questions(questions);
    }


    public Questions extractRandomQuestions(int size) {
        if (isZeroBiggerThan(size)) {
            throw new IllegalArgumentException("크키는 최소 1 이상입니다.");
        }

        ArrayList<Question> questionList = new ArrayList<>(questions);
        Collections.shuffle(questionList);
        List<Question> randomQuestions = questionList.subList(0, size);

        return from(randomQuestions);
    }


    private boolean isZeroBiggerThan(int size) {
        return size <= 0;
    }

    public List<Question> addQuestions(Questions newQuestions) {
        ArrayList<Question> mergedQuestions = new ArrayList<>(questions);
        mergedQuestions.addAll(newQuestions.questions);

        return mergedQuestions;
    }

}