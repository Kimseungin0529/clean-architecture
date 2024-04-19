package com.project.doongdoong.global.util;


import com.project.doongdoong.domain.analysis.dto.request.AnalysisEmotionRequestDto;
import com.project.doongdoong.domain.analysis.dto.response.FeelingStateResponseDto;
import com.project.doongdoong.domain.analysis.dto.response.FellingStateCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component @Slf4j
@RequiredArgsConstructor
public class WebClientUtil
{
    private final WebClient webClient;
    @Value("${spring.google.cloud.tts.url}")
    private String apiUrl;


    public FellingStateCreateResponse callLambdaApi(AnalysisEmotionRequestDto requestDto) {
        FellingStateCreateResponse result = null;
        try {
            Map<String, String> map = Map.of("text", requestDto.getText()); // 추후 맞춰서 변경하기

            FellingStateCreateResponse response = webClient.mutate().build()
                    .post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(map)
                    .retrieve()
                    .bodyToMono(FellingStateCreateResponse.class)
                    .block();

            if (response != null) {
                log.info("response.getFeelingState() = {}", response.getFeelingState());
                result = response;
            } else {
                log.error("Response body is null");
            }

        }catch (WebClientResponseException e){
            log.error("Error occurred: {}", e.getRawStatusCode());
            log.error("Response body: {}", e.getResponseBodyAsString());
        }


        return result != null ? result : null;
    }
}
