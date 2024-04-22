package com.project.doongdoong.domain.answer.service;

import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.answer.dto.AnswerCreateRequestDto;
import com.project.doongdoong.domain.answer.dto.AnswerCreateResponseDto;
import com.project.doongdoong.domain.answer.model.Answer;
import com.project.doongdoong.domain.answer.repository.AnswerRepository;
import com.project.doongdoong.domain.voice.dto.response.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.exception.VoiceUrlNotFoundException;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import com.project.doongdoong.domain.voice.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AnswerServiceImp implements AnswerService{

    private final VoiceRepository voiceRepository;
    private final VoiceService voiceService;
    private final AnswerRepository answerRepository;
    private final AnalysisRepository analysisRepository;
    private final WebClient webClient;

    @Transactional
    @Override
    public AnswerCreateResponseDto createAnswer(Long analysisId, AnswerCreateRequestDto dto) {
        Analysis findAnaylsis = analysisRepository.findById(analysisId).orElseThrow(() -> new AnalysisNotFoundException());

        VoiceDetailResponseDto voiceDto = voiceService.saveVoice(dto.getVoice());
        Voice voice = voiceRepository.findVoiceByAccessUrl(voiceDto.getAccessUrl()).orElseThrow(() -> new VoiceUrlNotFoundException());

        Answer answer = Answer.builder()
                .content(null)
                .voice(voice)
                .build();

        answer.connectAnalysis(findAnaylsis);
        answerRepository.save(answer);

        return AnswerCreateResponseDto.builder()
                .answerId(answer.getId())
                .build();
    }
}
