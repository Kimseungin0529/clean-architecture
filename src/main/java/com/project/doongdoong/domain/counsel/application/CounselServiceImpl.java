package com.project.doongdoong.domain.counsel.application;

import com.project.doongdoong.domain.analysis.application.port.out.AnalysisRepository;
import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AllAnswersNotFoundException;
import com.project.doongdoong.domain.analysis.exception.AnalysisAccessDeny;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.counsel.application.port.in.CounselService;
import com.project.doongdoong.domain.counsel.application.port.out.CounselRepository;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
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
import com.project.doongdoong.domain.user.adapter.out.persistence.UserJpaRepository;
import com.project.doongdoong.domain.user.domain.SocialIdentifier;
import com.project.doongdoong.domain.user.domain.UserEntity;
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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CounselServiceImpl implements CounselService {

    private final AnalysisRepository analysisRepository;
    private final CounselRepository counselRepository;
    private final UserJpaRepository userRepository;
    private final WebClientUtil webClientUtil;
    private final CounselRankingCache counselRankingCache;

    private final static int COUNSEL_PAGE_SIZE = 10;


    @Transactional
    @Override
    public CounselResultResponse consult(String uniqueValue, CounselCreateRequest request) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        UserEntity userEntity = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);

        CounselEntity counselEntity = CounselEntity.builder() // 상담 객체 생성
                .question(request.getQuestion())
                .counselType(CounselType.generateCounselTypeFrom(request.getCounselType()))
                .userEntity(userEntity)
                .build();

        if (request.getAnalysisId() != null) { // 기존 분석 결과 반영하기
            AnalysisEntity findAnalysisEntity = analysisRepository.findByUserAndId(userEntity, request.getAnalysisId()).orElseThrow(() -> new AnalysisAccessDeny());
            checkCounselAlreadyProcessed(findAnalysisEntity); // 해당 분석의 정보로 상담한 경우 예외
            counselEntity.addAnalysis(findAnalysisEntity); // 연관관계 매핑
        }

        HashMap<String, Object> parameters = setupParameters(counselEntity);

        CounselAiResponse counselAiResponse = webClientUtil.callConsult(parameters);

        counselEntity.saveAnswer(counselAiResponse.getAnswer());
        counselEntity.saveImageUrl(counselAiResponse.getImageUrl());
        CounselEntity savedCounselEntity = counselRepository.save(counselEntity);

        // TODO : 상담 유형 랭킹 도입
        counselRankingCache.incrementTotalCount(counselEntity.getCounselType());
        counselRankingCache.incrementTodayCount(counselEntity.getCounselType());


        return CounselResultResponse.builder()
                .counselId(savedCounselEntity.getId())
                .counselContent(counselAiResponse.getAnswer())
                .imageUrl(counselAiResponse.getImageUrl())
                .build();

    }

    private HashMap<String, Object> setupParameters(CounselEntity counselEntity) {
        HashMap<String, Object> parameters = new HashMap<String, Object>(); // 외부 API request 설정
        parameters.put("question", counselEntity.getQuestion()); // 고민은 필수
        parameters.put("category", counselEntity.getCounselType().getDescription());


        Optional.ofNullable(counselEntity.getAnalysis()) // 분석 -> 상담 으로 연결되는 경우, 분석에 대한 답변 항목 추가
                .ifPresent(analysis -> {
                    if (!analysis.hasAllAnswer()) {
                        throw new AllAnswersNotFoundException();
                    }
                    String content = getConcatenatedAnswerText(analysis);
                    parameters.put("analysisContent", content);
                });

        parameters.putIfAbsent("analysisContent", ""); // 없는 경우, 빈 문자열값
        return parameters;
    }

    @Override
    public CounselDetailResponse findCounselContent(String uniqueValue, Long counselId) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        UserEntity findUserEntity = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);
        CounselEntity findCounselEntity = counselRepository.findWithAnalysisById(counselId).orElseThrow(() -> new CounselNotFoundException());

        if (!findCounselEntity.getUser().getId().equals(findUserEntity.getId())) { // 사용자 본인의 상담만 확인 가능
            throw new UnAuthorizedForCounselException();
        }

        return CounselDetailResponse.builder()
                .date(findCounselEntity.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .counselId(findCounselEntity.getId())
                .question(findCounselEntity.getQuestion())
                .answer(findCounselEntity.getAnswer())
                .imageUrl(findCounselEntity.getImageUrl())
                .counselType(findCounselEntity.getCounselType().getDescription())
                .build();
    }

    @Override
    public CounselListResponse findCounsels(String uniqueValue, int pageNumber) {
        SocialIdentifier identifier = SocialIdentifier.from(uniqueValue);
        UserEntity findUserEntity = userRepository.findBySocialTypeAndSocialId(identifier.getSocialType(), identifier.getSocialId())
                .orElseThrow(UserNotFoundException::new);


        int pageIndex = convertPageIndexFrom(pageNumber);
        PageRequest pageRequest = PageRequest.of(pageIndex, COUNSEL_PAGE_SIZE);
        Page<CounselEntity> counselsPage = counselRepository.searchPageCounselList(findUserEntity, pageRequest);
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
                                        .date(counsel.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                        .counselId(counsel.getId())
                                        .isAnalysisUsed(counsel.hasAnalysis())
                                        .counselType(counsel.getCounselType().getDescription())
                                        .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }

    private void checkCounselAlreadyProcessed(AnalysisEntity findAnalysisEntity) {
        if (findAnalysisEntity.getCounsel() != null) {
            throw new CounselAlreadyProcessedException();
        }
    }

    private int convertPageIndexFrom(int pageNumber) {
        return pageNumber - 1;
    }


    private String getConcatenatedAnswerText(AnalysisEntity analysisEntity) {
        StringBuilder content = new StringBuilder();
        for (AnswerEntity answerEntity : analysisEntity.getAnswers()) {
            content.append(answerEntity.getContent()).append("\n");
        }
        return content.toString();
    }
}
