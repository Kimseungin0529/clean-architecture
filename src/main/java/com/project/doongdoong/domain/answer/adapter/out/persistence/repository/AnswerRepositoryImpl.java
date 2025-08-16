package com.project.doongdoong.domain.answer.adapter.out.persistence.repository;

import com.project.doongdoong.domain.analysis.adapter.out.persistence.AnalysisEntityMapper;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.voice.adapter.out.persistence.mapper.VoiceEntityMapper;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {
    private final AnswerJpaRepository answerJpaRepository;
    private final AnswerEntityMapper answerEntityMapper;
    private final AnalysisEntityMapper analysisEntityMapper;
    private final VoiceEntityMapper voiceEntityMapper;


    @Override
    public Answer save(Answer answer, Long analysisId, Long voiceId) {
        AnalysisEntity analysisEntity = analysisEntityMapper.fromId(analysisId);
        VoiceEntity voiceEntity = voiceEntityMapper.fromId(voiceId);

        AnswerEntity savedAnswerEntity = answerJpaRepository.save(answerEntityMapper.fromModel(answer, analysisEntity, voiceEntity));
        return answerEntityMapper.toModel(savedAnswerEntity);
    }

}
