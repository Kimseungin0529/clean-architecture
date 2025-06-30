package com.project.doongdoong.domain.answer.application;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.answer.adapter.in.dto.AnswerCreateResponseDto;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.answer.exception.AnswerConflictException;
import com.project.doongdoong.domain.answer.application.port.out.AnswerJpaRepository;
import com.project.doongdoong.domain.answer.application.port.in.AnswerService;
import com.project.doongdoong.domain.question.exception.NoMatchingQuestionException;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.voice.adapter.in.dto.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.exception.VoiceNotFoundException;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.application.port.in.VoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerServiceImp implements AnswerService {

    private final VoiceRepository voiceRepository;
    private final VoiceService voiceService;
    private final AnswerJpaRepository answerJpaRepository;
    private final AnalysisJpaRepository analysisJpaRepository;

    public final static int MAX_ANSWER_COUNT = 4;


    @Transactional
    @Override
    public AnswerCreateResponseDto createAnswer(Long analysisId, MultipartFile file, Long questionId) {
        Question matchedQuestion = findQuestionFromAnalysis(analysisId, questionId);

        if (matchedQuestion.hasAnswer()) { // 이미 설정된 question - answer이 존재할 때 다시 접근하려고 하면 예외 발생
            throw new AnswerConflictException();
        }
        Voice voice = saveVoiceFrom(file);
        AnswerEntity answerEntity = linkAndSaveToAnswer(voice, matchedQuestion);
        AnalysisEntity findAnalysisEntity = analysisJpaRepository.findById(analysisId).orElseThrow(AnalysisNotFoundException::new);
        answerEntity.connectAnalysis(findAnalysisEntity);

        return AnswerCreateResponseDto.builder()
                .answerId(answerEntity.getId())
                .build();
    }

    private AnswerEntity linkAndSaveToAnswer(Voice voice, Question matchedQuestion) {
        AnswerEntity answerEntity = AnswerEntity.builder()
                .content(null)
                .voice(voice)
                .build();

        matchedQuestion.connectAnswer(answerEntity);
        answerJpaRepository.save(answerEntity);
        return answerEntity;
    }

    private Voice saveVoiceFrom(MultipartFile file) {
        VoiceDetailResponseDto voiceDto = voiceService.saveVoice(file);
        return voiceRepository.findVoiceByAccessUrl(voiceDto.getAccessUrl()).orElseThrow(VoiceNotFoundException::new);
    }

    private Question findQuestionFromAnalysis(Long analysisId, Long questionId) {
        AnalysisEntity findAnalysisEntity = analysisJpaRepository.findAnalysisWithQuestion(analysisId)
                .orElseThrow(AnalysisNotFoundException::new);

        return findAnalysisEntity.getQuestions().stream()
                .filter(question -> question.getId() == (long) questionId)
                .findFirst().orElseThrow(NoMatchingQuestionException::new);
    }
}
