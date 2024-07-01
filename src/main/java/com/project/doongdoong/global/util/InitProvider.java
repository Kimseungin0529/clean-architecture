package com.project.doongdoong.global.util;

import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import com.project.doongdoong.domain.voice.service.VoiceService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component @Slf4j
@RequiredArgsConstructor
public class InitProvider {

    private final GoogleTtsProvider googleTtsProvider;
    private final VoiceRepository voiceRepository;
    private final VoiceService voiceService;

    @PostConstruct
    public void initQuestionVoiceFiles(){

        for (QuestionContent questionContent : QuestionContent.values()) {
            Optional<Voice> existingVoice = voiceRepository.findVoiceByQuestionContent(questionContent);
            if (!existingVoice.isPresent()) {
                byte[] audioContent = googleTtsProvider.convertTextToSpeech(questionContent.getText());
                String filename = "voice-question" + questionContent.getNumber();
                voiceService.saveTtsVoice(audioContent, filename, questionContent);
                log.info("Voice for question {} created and saved.", questionContent.getNumber());
            }
        }
    }
}
