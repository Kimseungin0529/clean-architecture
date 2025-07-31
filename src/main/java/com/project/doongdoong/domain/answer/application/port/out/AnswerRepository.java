package com.project.doongdoong.domain.answer.application.port.out;

import com.project.doongdoong.domain.answer.domain.Answer;

public interface AnswerRepository {

    Answer save(Answer answer);

    void deleteAnswersById(Long analysisId);

    void detachVoiceFromAnswersBy(Long analysisId);
}
