package com.project.doongdoong.domain.voice.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.io.FilenameUtils;
import com.project.doongdoong.domain.voice.dto.request.VoiceSaveRequestDto;
import com.project.doongdoong.domain.voice.dto.response.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.dto.response.VoicesResponseDto;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import com.project.doongdoong.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoiceService {

    private final String KEY = "voice/";

    @Value("${cloud.aws.bucket}")
    private String bucketName;
    private final AmazonS3Client amazonS3Client;
    private final VoiceRepository voiceRepository;
    @Transactional
    public VoicesResponseDto saveVoices(VoiceSaveRequestDto saveDto) {
        VoicesResponseDto resultList = new VoicesResponseDto();
        for(MultipartFile multipartFile : saveDto.getVoices()) {
            if(multipartFile.isEmpty()){
                throw new CustomException.InvalidRequestException(HttpStatus.BAD_REQUEST, "비어 있는 음성 파일이 있습니다.");
            }
            VoiceDetailResponseDto detailResponseDto = saveVoice(multipartFile);
            resultList.getVoicesResponse().add(detailResponseDto);
        }
        return resultList;
    }

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
            throw new CustomException.ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "음성 파일 저장 오류가 발생했습니다." +
                    " 에러 : " + e.getMessage());
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

    public void deleteVoice(String imageUrl) {
        Voice voice = voiceRepository.findByAccessUrl(imageUrl).
                orElseThrow(() -> new CustomException.NotFoundException(HttpStatus.NOT_FOUND, "해당 url은 존재하지 않습니다."));
        try{
            voiceRepository.delete(voice);
            amazonS3Client.deleteObject(bucketName, voice.getStoredName());
        }
        catch(SdkClientException e) {
            throw new CustomException.ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "음성 파일 삭제 오류가 발생했습니다." +
                    " 에러 : " + e.getMessage());
        }
    }

    @Transactional
    public void deleteVoice(List<String> voiceUrls) {
        for (String voiceUrl : voiceUrls) {
            deleteVoice(voiceUrl);
        }

    }


}
