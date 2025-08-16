package com.project.doongdoong.domain.question.application.port.dto;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.domain.Question;
import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisQuestionsAnswersDto {

    private final Analysis analysis;
    private final List<Question> questions;
    private final List<Answer> answers;

    public static AnalysisQuestionsAnswersDto of(Analysis analysis, List<Question> questions, List<Answer> answers) {
        return new AnalysisQuestionsAnswersDto(analysis, questions, answers);
    }

    private AnalysisQuestionsAnswersDto(Analysis analysis, List<Question> questions, List<Answer> answers) {
        this.analysis = analysis;
        this.questions = questions;
        this.answers = answers;
    }
}
