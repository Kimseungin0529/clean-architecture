package com.project.doongdoong.domain.answer.application;

import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.answer.adapter.in.dto.AnswerCreateResponseDto;
import com.project.doongdoong.domain.answer.application.port.in.AnswerService;
import com.project.doongdoong.domain.answer.application.port.out.AnswerRepository;
import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.answer.exception.AnswerConflictException;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.exception.NoMatchingQuestionException;
import com.project.doongdoong.domain.voice.adapter.in.dto.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.application.port.in.VoiceService;
import com.project.doongdoong.domain.voice.application.port.out.VoiceRepository;
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
    private final QuestionRepository questionRepository;

    public final static int MAX_ANSWER_COUNT = 4;


    @Transactional
    @Override
    public AnswerCreateResponseDto createAnswer(Long analysisId, MultipartFile file, Long questionId) {
        Question matchedQuestion = findQuestionFromAnalysis(analysisId, questionId);

        if (matchedQuestion.hasAnswer()) { // 이미 설정된 question - answer이 존재할 때 다시 접근하려고 하면 예외 발생
            throw new AnswerConflictException();
        }

        VoiceDetailResponseDto voiceDto = voiceService.saveVoice(file);
        Answer answer = Answer.of(voiceDto.getVoiceId());

        Answer savedAnswer = answerRepository.save(answer, analysisId, voiceDto.getVoiceId());

        return AnswerCreateResponseDto.builder()
                .answerId(savedAnswer.getId())
                .build();
    }

    private Question findQuestionFromAnalysis(Long analysisId, Long questionId) {

        return questionRepository.findQuestionsFrom(analysisId)
                .stream()
                .filter(question -> question.isSame(questionId))
                .findFirst().orElseThrow(NoMatchingQuestionException::new);
    }
}
