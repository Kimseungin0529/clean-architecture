package com.project.doongdoong.domain.question.application.port.dto;

import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.voice.domain.Voice;
import lombok.Getter;

import java.util.List;

@Getter
public class AnalysisTotalDto {

    private final Analysis analysis;
    private final List<Question> questions;
    private final List<Answer> answers;
    private final List<Voice> voices;

    public static AnalysisTotalDto of(Analysis analysis, List<Question> questions, List<Answer> answers, List<Voice> voices) {
        return new AnalysisTotalDto(analysis, questions, answers, voices);
    }

    private AnalysisTotalDto(Analysis analysis, List<Question> questions, List<Answer> answers, List<Voice> voices) {
        this.analysis = analysis;
        this.questions = questions;
        this.answers = answers;
        this.voices = voices;
    }
}
