package com.project.doongdoong.domain.voice.application.port.out;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.domain.Voice;

import java.util.List;
import java.util.Optional;

public interface VoiceRepository {
    Voice save(Voice voice);

    Optional<Voice> findVoiceByQuestionContent(QuestionContent questionContent);

    List<Voice> findVoiceAllByQuestionContentIn(List<QuestionContent> questionContent);

}
