package com.project.doongdoong.domain.voice.application.port.in;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.adapter.in.dto.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VoiceService {

    VoiceDetailResponseDto saveVoice(MultipartFile multipartFile);

    void saveVoice(byte[] audioContent, String originName, QuestionContent questionContent);


}
