package com.project.doongdoong.domain.voice.adapter.out.persistence;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
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
    public VoiceEntity save(VoiceEntity voiceEntity) {
        return voiceJpaRepository.save(voiceEntity);
    }

    @Override
    public Optional<VoiceEntity> findVoiceByQuestionContent(QuestionContent questionContent) {
        return voiceJpaRepository.findVoiceByQuestionContent(questionContent);
    }

    @Override
    public List<VoiceEntity> findVoiceAllByQuestionContentIn(List<QuestionContent> questionContent) {
        return voiceJpaRepository.findVoiceAllByQuestionContentIn(questionContent);
    }

    @Override
    public Optional<VoiceEntity> findVoiceByAccessUrl(String accessUrl) {
        return voiceJpaRepository.findVoiceByAccessUrl(accessUrl);
    }

    @Override
    public void deleteVoicesByUrls(List<Long> voiceIds) {
        voiceJpaRepository.deleteVoicesByUrls(voiceIds);
    }
}
