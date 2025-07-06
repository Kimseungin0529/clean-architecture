package com.project.doongdoong.domain.voice.application.port.out;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;

import java.util.List;
import java.util.Optional;

public interface VoiceRepository {
    VoiceEntity save(VoiceEntity voiceEntity);

    Optional<VoiceEntity> findVoiceByQuestionContent(QuestionContent questionContent);

    List<VoiceEntity> findVoiceAllByQuestionContentIn(List<QuestionContent> questionContent);

    Optional<VoiceEntity> findVoiceByAccessUrl(String accessUrl);

    void deleteVoicesByUrls(List<Long> voiceIds);
}
