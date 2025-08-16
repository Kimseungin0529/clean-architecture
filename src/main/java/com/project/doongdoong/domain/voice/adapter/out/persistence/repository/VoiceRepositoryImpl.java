package com.project.doongdoong.domain.voice.adapter.out.persistence.repository;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.adapter.out.persistence.mapper.VoiceEntityMapper;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.adapter.out.persistence.entity.VoiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoiceRepositoryImpl implements VoiceRepository {
    private final VoiceJpaRepository voiceJpaRepository;
    private final VoiceEntityMapper voiceEntityMapper;

    @Override
    public Voice save(Voice voice) {
        VoiceEntity voiceEntity = voiceJpaRepository.save(voiceEntityMapper.fromModel(voice));
        return voiceEntityMapper.toModel(voiceEntity);
    }

    @Override
    public Optional<Voice> findVoiceByQuestionContent(QuestionContent questionContent) {
        return voiceJpaRepository.findVoiceByQuestionContent(questionContent)
                .map(voiceEntityMapper::toModel);
    }

    @Override
    public List<Voice> findVoiceAllByQuestionContentIn(List<QuestionContent> questionContent) {
        return voiceJpaRepository.findVoiceAllByQuestionContentIn(questionContent)
                .stream().map(voiceEntityMapper::toModel)
                .toList();
    }

}
