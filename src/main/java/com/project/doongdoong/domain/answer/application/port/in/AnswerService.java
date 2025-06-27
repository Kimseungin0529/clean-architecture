package com.project.doongdoong.domain.answer.application.port.in;


import com.project.doongdoong.domain.answer.adapter.in.dto.AnswerCreateResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface AnswerService {
    public AnswerCreateResponseDto createAnswer(Long analysisId, MultipartFile multipartFile, Long questionId);
}
