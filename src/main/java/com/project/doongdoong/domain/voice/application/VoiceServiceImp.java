package com.project.doongdoong.domain.voice.application;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.voice.adapter.in.dto.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.domain.VoiceEntity;
import com.project.doongdoong.domain.voice.exception.FileUploadException;
import com.project.doongdoong.domain.voice.domain.FileExtension;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.application.port.in.VoiceService;
import com.project.doongdoong.global.exception.ErrorType;
import com.project.doongdoong.global.exception.servererror.ExternalApiCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.project.doongdoong.domain.voice.domain.FileExtension.MP3;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoiceServiceImp implements VoiceService {

    private final AmazonS3Client amazonS3Client;
    private final VoiceRepository voiceRepository;

    @Value("${cloud.aws.bucket}")
    private String bucketName;
    private static final String VOICE_KEY = "voice/";


    @Override
    @Transactional
    public VoiceDetailResponseDto saveVoice(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        VoiceEntity voiceEntity = VoiceEntity.commonBuilder()
                .originName(originalName)
                .build();

        String filename = getObjectKeyFrom(voiceEntity);
        try {
            ObjectMetadata objectMetadata = initObjectMetadata(originalName, multipartFile);
            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);
            voiceEntity.changeAccessUrl(amazonS3Client.getUrl(bucketName, filename).toString());

        } catch (SdkClientException | IOException e) {
            throw new FileUploadException(ErrorType.ServerError.FILE_UPLOAD_FAIL, e.getMessage());
        }
        voiceRepository.save(voiceEntity);

        return VoiceDetailResponseDto.of(voiceEntity.getAccessUrl());
    }

    @Override
    @Transactional
    public void saveVoice(byte[] audioContent, String originName, QuestionContent questionContent) {
        VoiceEntity voiceEntity = VoiceEntity.initVoiceContentBuilder()
                .originName(originName)
                .questionContent(questionContent)
                .build();
        String filename = getObjectKeyFrom(voiceEntity);

        try {
            ObjectMetadata objectMetadata = initObjectMetadata(audioContent);
            amazonS3Client.putObject(bucketName, filename, new ByteArrayInputStream(audioContent), objectMetadata);

            voiceEntity.changeAccessUrl(amazonS3Client.getUrl(bucketName, filename).toString());
            voiceRepository.save(voiceEntity);

        } catch (SdkClientException e) {
            throw new FileUploadException(ErrorType.ServerError.FILE_UPLOAD_FAIL, e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteVoices(List<VoiceEntity> voiceEntities) {
        List<Long> voiceIds = getIdsFrom(voiceEntities);
        if (voiceIds.isEmpty()) {
            return;
        }

        List<DeleteObjectsRequest.KeyVersion> keys = getRequestKeysFrom(voiceEntities);
        try {
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(keys);
            amazonS3Client.deleteObjects(deleteRequest);

        } catch (SdkClientException e) {
            throw new ExternalApiCallException("S3 파일 삭제 실패 : " + e.getMessage());
        }
        voiceRepository.deleteVoicesByUrls(voiceIds);
    }

    private ObjectMetadata initObjectMetadata(String originalName, MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(FileExtension.getMineTypeFrom(originalName));
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        return objectMetadata;
    }

    private ObjectMetadata initObjectMetadata(byte[] content) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MP3.getMineType());
        objectMetadata.setContentLength(content.length);
        return objectMetadata;
    }


    private List<Long> getIdsFrom(List<VoiceEntity> voiceEntities) {
        return voiceEntities.stream().map(VoiceEntity::getVoiceId).toList();
    }

    private List<DeleteObjectsRequest.KeyVersion> getRequestKeysFrom(List<VoiceEntity> voiceEntities) {
        return voiceEntities.stream()
                .map(voice -> new DeleteObjectsRequest.KeyVersion(getObjectKeyFrom(voice)))
                .toList();
    }

    private String getObjectKeyFrom(VoiceEntity voiceEntity) {
        return VOICE_KEY + voiceEntity.getStoredName();
    }


}
