package com.project.doongdoong.domain.answer.application;

import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.exception.AnalysisNotFoundException;
import com.project.doongdoong.domain.answer.adapter.in.dto.AnswerCreateResponseDto;
import com.project.doongdoong.domain.answer.application.port.in.AnswerService;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.exception.AnswerConflictException;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.exception.NoMatchingQuestionException;
import com.project.doongdoong.domain.voice.adapter.in.dto.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.application.port.in.VoiceService;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
import com.project.doongdoong.domain.voice.domain.Voice;
import com.project.doongdoong.domain.voice.exception.VoiceNotFoundException;
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
    private final AnswerRepository answerRepository;
    private final AnalysisRepository analysisRepository;

    public final static int MAX_ANSWER_COUNT = 4;


    @Transactional
    @Override
    public AnswerCreateResponseDto createAnswer(Long analysisId, MultipartFile file, Long questionId) {
        Question matchedQuestion = findQuestionFromAnalysis(analysisId, questionId);

        if (matchedQuestion.hasAnswer()) { // 이미 설정된 question - answer이 존재할 때 다시 접근하려고 하면 예외 발생
            throw new AnswerConflictException();
        }
        Voice voice = saveVoiceFrom(file);
        Answer answer = linkWith(voice, matchedQuestion);
        answerRepository.save(answer);

        return AnswerCreateResponseDto.builder()
                .answerId(answer.getId())
                .build();
    }

    private Voice saveVoiceFrom(MultipartFile file) {
        VoiceDetailResponseDto voiceDto = voiceService.saveVoice(file);
        return voiceRepository.findVoiceByAccessUrl(voiceDto.getAccessUrl()).orElseThrow(VoiceNotFoundException::new);
    }

    private Answer linkWith(Voice voice, Question matchedQuestion) {
        Answer answer = Answer.of(voice);
        matchedQuestion.connectAnswer(answer);
        return answer;
    }

    private Question findQuestionFromAnalysis(Long analysisId, Long questionId) {
        Analysis findAnalysis = analysisRepository.findAnalysisWithQuestion(analysisId)
                .orElseThrow(AnalysisNotFoundException::new);

        return findAnalysis.getQuestions().stream()
                .filter(question -> question.getId() == (long) questionId)
                .findFirst().orElseThrow(NoMatchingQuestionException::new);
    }
}
