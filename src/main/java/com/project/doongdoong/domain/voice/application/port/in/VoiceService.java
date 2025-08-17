package com.project.doongdoong.domain.voice.application.port.in;

import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.adapter.in.dto.VoiceDetailResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface VoiceService {

    VoiceDetailResponseDto saveVoice(MultipartFile multipartFile);

    void saveVoice(byte[] audioContent, String originName, QuestionContent questionContent);


}
