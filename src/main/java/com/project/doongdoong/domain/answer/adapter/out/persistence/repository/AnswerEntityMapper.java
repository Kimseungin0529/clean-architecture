package com.project.doongdoong.domain.answer.adapter.out.persistence.repository;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import org.springframework.stereotype.Component;

@Component
public class AnswerEntityMapper {

    public Answer toModel(AnswerEntity answerEntity) {
        return Answer.builder()
                .id(answerEntity.getId())
                .content(answerEntity.getContent())
                .build();
    }


    public Answer toModel(AnswerEntity answerEntity, Voice voice) {
        return Answer.builder()
                .id(answerEntity.getId())
                .content(answerEntity.getContent())
                .voiceId(voice.getVoiceId())
                .build();
    }

    public AnswerEntity fromModel(Answer answer, AnalysisEntity analysisEntity, VoiceEntity voiceEntity) {
        return AnswerEntity.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .analysis(analysisEntity)
                .voice(voiceEntity)
                .build();
    }
}
