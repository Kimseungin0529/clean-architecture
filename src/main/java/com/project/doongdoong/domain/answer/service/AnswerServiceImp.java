package com.project.doongdoong.domain.answer.service;

import com.project.doongdoong.domain.answer.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AnswerServiceImp implements AnswerService{

    private final AnswerRepository answerRepository;
    private final WebClient webClient;

    @Override
    public void createAnswer(MultipartFile multipartFile) {
        // mu
    }
}
