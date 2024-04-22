package com.project.doongdoong.domain.voice.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.cloud.texttospeech.v1.*;
import com.project.doongdoong.domain.image.exception.FileDeleteException;
import com.project.doongdoong.domain.image.exception.FileEmptyException;
import com.project.doongdoong.domain.image.exception.FileUploadException;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.voice.exception.VoiceUrlNotFoundException;
import org.apache.commons.io.FilenameUtils;
import com.project.doongdoong.domain.voice.dto.request.VoiceSaveRequestDto;
import com.project.doongdoong.domain.voice.dto.response.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.dto.response.VoicesResponseDto;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoiceServiceImp implements VoiceService{

    private static final String KEY = "voice/";

    @Value("${cloud.aws.bucket}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;
    private final VoiceRepository voiceRepository;
    @Override
    @Transactional
    public VoicesResponseDto saveVoices(VoiceSaveRequestDto saveDto) {
        VoicesResponseDto resultList = new VoicesResponseDto();
        for(MultipartFile multipartFile : saveDto.getVoices()) {
            if(multipartFile.isEmpty()){
                new FileEmptyException();
            }
            VoiceDetailResponseDto detailResponseDto = saveVoice(multipartFile);
            resultList.getVoicesResponse().add(detailResponseDto);
        }
        return resultList;
    }


    @Override
    public VoiceDetailResponseDto saveVoice(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        Voice voice = new Voice(originalName);
        String filename = KEY + voice.getStoredName();

        log.info("음성 파일 저장 시작");
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            String contentType = getContentTypeFromFilename(originalName); // 확장자를 기반으로 MIME 타입 결정
            objectMetadata.setContentType(contentType); // MIME 타입 설정
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            voice.changeAccessUrl(accessUrl);
        } catch(SdkClientException | IOException e) {
            log.error("음성 파일 업로드 오류 -> {}", e.getMessage());
            new FileUploadException();
        }
        log.info("음성 파일 저장 종료");

        voiceRepository.save(voice);

        return VoiceDetailResponseDto.of(voice.getAccessUrl());
    }

    private String getContentTypeFromFilename(String filename) {
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        switch (extension) {
            case "mp3":
                return "audio/mpeg";
            case "m4a":
                return "audio/mp4";
            default:
                throw new IllegalArgumentException("Unsupported file format");
        }
    }

    @Override
    public void deleteVoice(String imageUrl) {
        Voice voice = voiceRepository.findByAccessUrl(imageUrl).orElseThrow(() -> new VoiceUrlNotFoundException());
        try{
            voiceRepository.delete(voice);
            amazonS3Client.deleteObject(bucketName, voice.getStoredName());
        }
        catch(SdkClientException e) {
            log.error("음성 파일 삭제 오류 -> {}", e.getMessage());
            new FileDeleteException();
        }
    }

    @Override
    @Transactional
    public void deleteVoice(List<String> voiceUrls) {
        for (String voiceUrl : voiceUrls) {
            deleteVoice(voiceUrl);
        }

    }

    @Override
    public VoiceDetailResponseDto saveTtsVoice(byte[] audioContent, String originName, QuestionContent questionContent) {

        Voice voice = new Voice(originName, questionContent);
        String filename = KEY + voice.getStoredName();

        try {
            log.info("TTS 음성 파일 저장 시작");
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("audio/mpeg"); // MP3 파일의 MIME 타입 설정
            objectMetadata.setContentLength(audioContent.length);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioContent);
            amazonS3Client.putObject(bucketName, filename, byteArrayInputStream, objectMetadata);

            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            voice.changeAccessUrl(accessUrl);
            voiceRepository.save(voice);

        } catch(SdkClientException e) {
            log.error("TTS 음성 파일 업로드 오류 -> {}", e.getMessage());
            throw new FileUploadException();
        }

        return VoiceDetailResponseDto.of(voice.getAccessUrl());
    }

}
