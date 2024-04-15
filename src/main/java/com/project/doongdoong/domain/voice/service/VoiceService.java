package com.project.doongdoong.domain.voice.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.doongdoong.domain.image.exception.FileDeleteException;
import com.project.doongdoong.domain.image.exception.FileEmptyException;
import com.project.doongdoong.domain.image.exception.FileUploadException;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.voice.dto.request.VoiceSaveRequestDto;
import com.project.doongdoong.domain.voice.dto.response.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.dto.response.VoicesResponseDto;
import com.project.doongdoong.domain.voice.exception.VoiceUrlNotFoundException;
import com.project.doongdoong.domain.voice.model.Voice;
import org.apache.commons.io.FilenameUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface VoiceService {
    public VoicesResponseDto saveVoices(VoiceSaveRequestDto saveDto);

    public VoiceDetailResponseDto saveVoice(MultipartFile multipartFile);

    public void deleteVoice(String imageUrl);

    public void deleteVoice(List<String> voiceUrls);

    public VoiceDetailResponseDto saveTtsVoice(byte[] bytes, String originName, QuestionContent questionContent);

}
