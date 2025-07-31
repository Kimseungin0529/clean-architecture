package com.project.doongdoong.domain.voice.adapter.out.persistence;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoiceRepositoryImpl implements VoiceRepository {
    private final VoiceJpaRepository voiceJpaRepository;

    @Override
    public Voice save(Voice voice) {
        return voiceJpaRepository.save(VoiceEntity.fromModel(voice)).toModel();
    }

    @Override
    public Optional<Voice> findVoiceByQuestionContent(QuestionContent questionContent) {
        return voiceJpaRepository.findVoiceByQuestionContent(questionContent)
                .map(VoiceEntity::toModel);
    }

    @Override
    public List<Voice> findVoiceAllByQuestionContentIn(List<QuestionContent> questionContent) {
        return voiceJpaRepository.findVoiceAllByQuestionContentIn(questionContent)
                .stream().map(VoiceEntity::toModel)
                .toList();
    }

    @Override
    public Optional<Voice> findVoiceByAccessUrl(String accessUrl) {
        return voiceJpaRepository.findVoiceByAccessUrl(accessUrl)
                .map(VoiceEntity::toModel);
    }

    @Override
    public void deleteVoicesByUrls(List<Long> voiceIds) {
        voiceJpaRepository.deleteVoicesByUrls(voiceIds);
    }
}
