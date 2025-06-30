package com.project.doongdoong.domain.answer.application.port.out;

import com.project.doongdoong.domain.answer.domain.AnswerEntity;

public interface AnswerRepository {

    AnswerEntity save(AnswerEntity answerEntity);

    void deleteAnswersById(Long analysisId);

    void detachVoiceFromAnswersBy(Long analysisId);
}
