package com.project.doongdoong.domain.counsel.application;

import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.Analysis;
import com.project.doongdoong.domain.analysis.exception.AnalysisAccessDeny;
import com.project.doongdoong.domain.counsel.application.port.in.CounselService;
import com.project.doongdoong.domain.counsel.application.port.out.CounselRepository;
import com.project.doongdoong.domain.counsel.domain.Counsel;
import com.project.doongdoong.domain.counsel.domain.CounselType;
import com.project.doongdoong.domain.counsel.dto.request.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.response.CounselDetailResponse;
import com.project.doongdoong.domain.counsel.dto.response.CounselListResponse;
import com.project.doongdoong.domain.counsel.dto.response.CounselResponse;
import com.project.doongdoong.domain.counsel.dto.response.CounselResultResponse;
import com.project.doongdoong.domain.counsel.exception.CounselAlreadyProcessedException;
import com.project.doongdoong.domain.counsel.exception.CounselNotExistPageException;
import com.project.doongdoong.domain.counsel.exception.CounselNotFoundException;
import com.project.doongdoong.domain.counsel.exception.UnAuthorizedForCounselException;
import com.project.doongdoong.domain.question.application.port.dto.QuestionAnswer;
import com.project.doongdoong.domain.question.application.port.out.QuestionRepository;
import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.domain.user.domain.SocialIdentifier;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.global.dto.response.CounselAiResponse;
import com.project.doongdoong.global.util.CounselRankingCache;
import com.project.doongdoong.global.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CounselServiceImpl implements CounselService {

    private final AnalysisRepository analysisRepository;
    private final QuestionRepository questionRepository;
    private final CounselRepository counselRepository;
    private final UserRepository userRepository;
    private final WebClientUtil webClientUtil;
    private final CounselRankingCache counselRankingCache;

    private final static int COUNSEL_PAGE_SIZE = 10;


    @Transactional
    @Override
    public CounselResultResponse consult(String uniqueValue, CounselCreateRequest request) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        User user = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);

        Counsel counsel = Counsel.of(request.getQuestion(), CounselType.generateCounselTypeFrom(request.getCounselType()), user.getId(), LocalDateTime.now());

        if (request.getAnalysisId() != null) { // 기존 분석 결과 반영하기
            Analysis findAnalysis = analysisRepository.findByUserIdAndId(user.getId(), request.getAnalysisId())
                    .orElseThrow(AnalysisAccessDeny::new);
            checkCounselAlreadyProcessed(findAnalysis); // 해당 분석의 정보로 상담한 경우 예외
            counsel.addAnalysisId(findAnalysis.getId());
        }

        HashMap<String, Object> parameters = setupParameters(counsel);
        CounselAiResponse counselAiResponse = webClientUtil.callConsult(parameters);

        counsel.saveAnswer(counselAiResponse.getAnswer());
        counsel.saveImageUrl(counselAiResponse.getImageUrl());
        Counsel savedCounsel = counselRepository.save(counsel);

        counselRankingCache.incrementTotalCount(counsel.getCounselType());
        counselRankingCache.incrementTodayCount(counsel.getCounselType());


        return CounselResultResponse.builder()
                .counselId(savedCounsel.getId())
                .counselContent(counselAiResponse.getAnswer())
                .imageUrl(counselAiResponse.getImageUrl())
                .build();

    }

    @Override
    public CounselDetailResponse findCounselContent(String uniqueValue, Long counselId) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        User findUser = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);
        Counsel findCounsel = counselRepository.findCounselWithUserById(counselId).orElseThrow(CounselNotFoundException::new);

        if (!findCounsel.getUserId().equals(findUser.getId())) { // 사용자 본인의 상담만 확인 가능
            throw new UnAuthorizedForCounselException();
        }

        return CounselDetailResponse.builder()
                .date(findCounsel.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .counselId(findCounsel.getId())
                .question(findCounsel.getQuestion())
                .answer(findCounsel.getAnswer())
                .imageUrl(findCounsel.getImageUrl())
                .counselType(findCounsel.getCounselType().getDescription())
                .build();
    }

    @Override
    public CounselListResponse findCounsels(String uniqueValue, int pageNumber) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        User findUser = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);


        int pageIndex = convertPageIndexFrom(pageNumber);
        PageRequest pageRequest = PageRequest.of(pageIndex, COUNSEL_PAGE_SIZE);
        Page<Counsel> counselsPage = counselRepository.searchPageCounselList(findUser, pageRequest);
        if (pageNumber > counselsPage.getTotalPages()) { // 존재하지 않는 페이지에 접근하는 경우
            throw new CounselNotExistPageException();
        }

        return CounselListResponse.builder()
                .currentPage(counselsPage.getNumber() + 1)
                .numberPerPage(counselsPage.getSize())
                .totalPage(counselsPage.getTotalPages())
                .totalElements(counselsPage.getTotalElements())
                .counselContent(counselsPage.getContent().stream()
                        .map(counsel ->
                                CounselResponse.builder()
                                        .date(counsel.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                        .counselId(counsel.getId())
                                        .isAnalysisUsed(counsel.hasAnalysis())
                                        .counselType(counsel.getCounselType().getDescription())
                                        .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    private void checkCounselAlreadyProcessed(Analysis findAnalysis) {
        if (findAnalysis.isUsed()) {
            throw new CounselAlreadyProcessedException();
        }
    }

    private HashMap<String, Object> setupParameters(Counsel counsel) {
        HashMap<String, Object> parameters = new HashMap<>(); // 외부 API request 설정
        parameters.put("question", counsel.getQuestion()); // 고민은 필수
        parameters.put("category", counsel.getCounselType().getDescription());


        Optional.ofNullable(counsel.getAnalysisId()) // 분석 -> 상담 으로 연결되는 경우, 분석에 대한 답변 항목 추가
                .ifPresent(analysisId -> {
                    String content = getConcatenatedAnswerText(analysisId);
                    parameters.put("analysisContent", content);
                });

        parameters.putIfAbsent("analysisContent", ""); // 없는 경우, 빈 문자열값
        return parameters;
    }

    private int convertPageIndexFrom(int pageNumber) {
        return pageNumber - 1;
    }


    private String getConcatenatedAnswerText(Long analysisId) {
        List<QuestionAnswer> questionAnswers = questionRepository.findQuestionsByAnalysisIdWithAnswer(analysisId);
        StringBuilder content = new StringBuilder();

        questionAnswers.stream()
                .map(QuestionAnswer::getAnswer)
                .forEach(answer -> content.append(answer.getContent()).append("\n"));

        return content.toString();
    }
}
