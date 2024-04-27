package com.project.doongdoong.global.util;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.project.doongdoong.domain.analysis.dto.response.FellingStateCreateResponse;
import com.project.doongdoong.domain.voice.model.Voice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component @Slf4j
@RequiredArgsConstructor
public class WebClientUtil
{
    private final WebClient webClient;
    private final AmazonS3Client amazonS3Client;

    @Value("${spring.google.cloud.tts.url}")
    private String apiUrl;
    @Value("${cloud.aws.bucket}")
    private String bucketName;
    private static final String VOICE_KEY = "voice/";



    public List<FellingStateCreateResponse> callAnalyzeEmotion(List<Voice> voices) {

        Flux<FellingStateCreateResponse> responseFlux = Flux
                .fromIterable(voices)
                .flatMap(voice -> callLambdaApi(voice));

        List<FellingStateCreateResponse> response = responseFlux.collectList().block();
        return response;
    }

    private Mono<FellingStateCreateResponse> callLambdaApi(Voice voice) {

        try {
            // S3에서 음성 파일 가져오기
            S3ObjectInputStream inputStream = convertVoiceToFileData(voice);
            byte[] fileBytes = IOUtils.toByteArray(inputStream);

            // MultipartBodyBuilder를 사용하여 파일 업로드 요청 생성
            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
            multipartBodyBuilder.part("file", fileBytes, MediaType.APPLICATION_OCTET_STREAM);
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

             return webClient.mutate().build()
                    .post()
                    .uri(apiUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(BodyInserters.fromMultipartData(multipartBody))
                    .retrieve()
                    .bodyToMono(FellingStateCreateResponse.class);

        }catch (WebClientResponseException e){
            log.error("Error occurred: {}", e.getRawStatusCode());
            log.error("Response body: {}", e.getResponseBodyAsString());
        } catch (AmazonS3Exception e) {
            log.error("AmazonS3Exception occurred: {}", e.getMessage());
            log.error("AmazonS3Exception status code: {}", e.getStatusCode());
            throw new RuntimeException("Failed to retrieve voice file from S3", e);
        }  catch (IOException e) {
            throw new RuntimeException("byte 변환에 실패했습니다.",e);
        }

        return null;
    }
    private S3ObjectInputStream convertVoiceToFileData(Voice voice) {
        S3Object s3Object = amazonS3Client.getObject(bucketName, VOICE_KEY + voice.getStoredName());
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return inputStream;
    }

}
