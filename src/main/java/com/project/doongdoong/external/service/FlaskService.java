package com.project.doongdoong.external.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doongdoong.external.dto.request.EmotionInfoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service @Slf4j
@RequiredArgsConstructor
public class FlaskService {

    private final WebClient webClient;

    public String predictEmotion(EmotionInfoRequestDto dto)  {
        log.info("flask api 호출 준비");
        Mono<String> result = webClient.post()
                .uri("/emotion")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
        log.info("flask api 호출 종료");

        return result.block();

    }

}
/* RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String cotent = dto.getCotent();

        //파라미터로 들어온 dto를 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> entity = new HttpEntity<String>(body , headers);

        //실제 Flask 서버랑 연결하기 위한 URL
        String url = "http://127.0.0.1:5000/flask/classity-emotion";

        //Flask 서버로 데이터를 전송하고 받은 응답 값을 return
        return restTemplate.postForObject(url, entity, String.class);*/
