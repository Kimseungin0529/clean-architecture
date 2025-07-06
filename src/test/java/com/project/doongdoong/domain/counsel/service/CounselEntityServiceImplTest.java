package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.analysis.exception.AnalysisAccessDeny;
import com.project.doongdoong.domain.analysis.adapter.out.persistence.repository.AnalysisJpaRepository;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.counsel.application.CounselServiceImpl;
import com.project.doongdoong.domain.counsel.dto.request.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.response.CounselDetailResponse;
import com.project.doongdoong.domain.counsel.dto.response.CounselListResponse;
import com.project.doongdoong.domain.counsel.dto.response.CounselResultResponse;
import com.project.doongdoong.domain.counsel.exception.CounselNotExistPageException;
import com.project.doongdoong.domain.counsel.exception.UnAuthorizedForCounselException;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.counsel.domain.CounselType;
import com.project.doongdoong.domain.counsel.adapter.out.CounselJpaRepository;
import com.project.doongdoong.domain.user.domain.SocialType;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.domain.user.application.port.out.UserRepository;
import com.project.doongdoong.global.dto.response.CounselAiResponse;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class CounselEntityServiceImplTest extends IntegrationSupportTest {
    @Autowired
    CounselServiceImpl counselService;
    @Autowired
    AnalysisJpaRepository analysisRepository;
    @Autowired
    CounselJpaRepository counselRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원의 질문에 대한 상담 답변을 제공한다.")
    void consult() {
        //given
        String socialId = "123456";
        UserEntity userEntity = createUser(socialId, SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        CounselCreateRequest request = new CounselCreateRequest(
                "취업진로",
                "나는 취업에 대한 고민이 있어. 내가 개발자가 될 수 있을까? 어떤 노력이 필요해?"
        );
        CounselAiResponse mockResponse = new CounselAiResponse(
                "개발자가 되기 위해서는 꾸준한 학습과 프로젝트 경험이 필요합니다.",
                "http://example.com/image.jpg"
        );
        String uniqueValue = savedUserEntity.getSocialId() + "_" + savedUserEntity.getSocialType().getDescription();

        when(webClientUtil.callConsult(any(HashMap.class)))
                .thenReturn(mockResponse);
        //when
        CounselResultResponse result = counselService.consult(uniqueValue, request);

        //then
        assertThat(result).isNotNull()
                .extracting("counselContent", "imageUrl")
                .containsExactly(mockResponse.getAnswer(), mockResponse.getImageUrl());
        assertThat(result.getCounselId()).isNotNull();
    }

    @Test
    @DisplayName("분석 답변과 함께 회원의 질문에 대한 상담 답변을 제공한다.")
    void consultWithAnalysis() {
        //given
        String socialId = "123456";
        UserEntity userEntity = UserEntity.builder()
                .socialId(socialId)
                .socialType(SocialType.APPLE)
                .build();
        UserEntity savedUserEntity = userRepository.save(userEntity);

        AnalysisEntity analysisEntity = createAnalysis(savedUserEntity);
        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);

        AnswerEntity answerEntity1 = createAnswer();
        AnswerEntity answerEntity2 = createAnswer();
        AnswerEntity answerEntity3 = createAnswer();
        AnswerEntity answerEntity4 = createAnswer();
        answerEntity1.connectAnalysis(savedAnalysisEntity);
        answerEntity2.connectAnalysis(savedAnalysisEntity);
        answerEntity3.connectAnalysis(savedAnalysisEntity);
        answerEntity4.connectAnalysis(savedAnalysisEntity);

        CounselCreateRequest request = new CounselCreateRequest(
                savedAnalysisEntity.getId(),
                "취업진로",
                "나는 취업에 대한 고민이 있어. 내가 개발자가 될 수 있을까? 어떤 노력이 필요해?"
        );
        CounselAiResponse mockResponse = new CounselAiResponse(
                "개발자가 되기 위해서는 꾸준한 학습과 프로젝트 경험이 필요합니다.",
                "http://example.com/image.jpg"
        );
        String uniqueValue = savedUserEntity.getSocialId() + "_" + savedUserEntity.getSocialType().getDescription();

        when(webClientUtil.callConsult(any(HashMap.class)))
                .thenReturn(mockResponse);
        //when
        CounselResultResponse result = counselService.consult(uniqueValue, request);

        //then
        assertThat(result).isNotNull()
                .extracting("counselContent", "imageUrl")
                .containsExactly(mockResponse.getAnswer(), mockResponse.getImageUrl());
        assertThat(result.getCounselId()).isNotNull();

    }


    @Test
    @DisplayName("다른 사용자의 분석 정보를 통해 상담할 수 없습니다.")
    void exceptionWhenAnalysisDoesNotBelongToUser() {
        //given
        String socialId = "123456";
        UserEntity userEntity = UserEntity.builder()
                .socialId(socialId)
                .socialType(SocialType.APPLE)
                .build();
        String socialId2 = "455678";
        UserEntity otherUserEntity = createUser(socialId2, SocialType.APPLE);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserEntity savedOtherUserEntity = userRepository.save(otherUserEntity);

        AnalysisEntity analysisEntity = AnalysisEntity.builder()
                .userEntity(savedUserEntity)
                .build();
        AnalysisEntity savedAnalysisEntity = analysisRepository.save(analysisEntity);
        AnswerEntity answerEntity1 = createAnswer();
        AnswerEntity answerEntity2 = createAnswer();
        AnswerEntity answerEntity3 = createAnswer();
        AnswerEntity answerEntity4 = createAnswer();
        answerEntity1.connectAnalysis(savedAnalysisEntity);
        answerEntity2.connectAnalysis(savedAnalysisEntity);
        answerEntity3.connectAnalysis(savedAnalysisEntity);
        answerEntity4.connectAnalysis(savedAnalysisEntity);

        CounselCreateRequest request = new CounselCreateRequest(
                savedAnalysisEntity.getId(),
                "취업진로",
                "나는 취업에 대한 고민이 있어. 내가 개발자가 될 수 있을까? 어떤 노력이 필요해?"
        );
        CounselAiResponse mockResponse = new CounselAiResponse(
                "개발자가 되기 위해서는 꾸준한 학습과 프로젝트 경험이 필요합니다.",
                "http://example.com/image.jpg"
        );
        String otherUniqueValue = savedOtherUserEntity.getSocialId() + "_" + savedOtherUserEntity.getSocialType().getDescription();

        when(webClientUtil.callConsult(any(HashMap.class)))
                .thenReturn(mockResponse);

        //when & then
        assertThatThrownBy(() -> counselService.consult(otherUniqueValue, request))
                .isInstanceOf(AnalysisAccessDeny.class)
                .hasMessageContaining("해당 사용자의 분석이 아니거나 존재하지 않는 분석입니다."); // 예외 메시지는 비즈니스 로직에 맞게 설정


    }


    @DisplayName("상담 내용을 조회하는 시나리오")
    @TestFactory
    Collection<DynamicTest> findCounselContent() {
        // given
        String socialId1 = "123456";
        String socialId2 = "542156";
        UserEntity userEntity1 = createUser(socialId1, SocialType.APPLE);
        UserEntity userEntity2 = createUser(socialId2, SocialType.APPLE);

        CounselEntity counselEntity = createCounsel(userEntity1, "상담 질문 내용", CounselType.LOVE);
        userRepository.saveAll(List.of(userEntity1, userEntity2));
        CounselEntity savedCounselEntity = counselRepository.save(counselEntity);

        // when & then
        return List.of(
                DynamicTest.dynamicTest("상담 내용을 조회합니다.", () -> {
                            //given
                            String uniqueValue = userEntity1.getSocialId() + "_" + userEntity1.getSocialType().getDescription();
                            //when
                            CounselDetailResponse result = counselService.findCounselContent(uniqueValue, savedCounselEntity.getId());
                            //then
                            assertThat(result)
                                    .extracting("counselId", "question", "answer", "counselType")
                                    .containsExactly(savedCounselEntity.getId(), "상담 질문 내용", savedCounselEntity.getAnswer(), CounselType.LOVE.getDescription());
                        }

                ),
                DynamicTest.dynamicTest("남의 상담 내용을 조회할 수 없습니다.", () -> {
                    // given
                    String otherUniqueValue = userEntity2.getSocialId() + "_" + userEntity2.getSocialType().getDescription();
                    // when & then
                    assertThatThrownBy(() ->
                            counselService.findCounselContent(otherUniqueValue, savedCounselEntity.getId()))
                            .isInstanceOf(UnAuthorizedForCounselException.class)
                            .hasMessage("다른 사용자의 상담입니다.");

                })
        );

    }

    @DisplayName("상담 목록을 조회 시나리오")
    @TestFactory
    Collection<DynamicTest> findCounsels_시나리오() {
        // given
        String socialId = "123456";
        SocialType socialType = SocialType.APPLE;
        UserEntity userEntity = createUser(socialId, socialType);
        userRepository.save(userEntity);

        CounselEntity counselEntity1 = createCounsel(userEntity, "상담 질문 내용1", CounselType.LOVE);
        CounselEntity counselEntity2 = createCounsel(userEntity, "상담 질문 내용2", CounselType.JOB);
        CounselEntity counselEntity3 = createCounsel(userEntity, "상담 질문 내용3", CounselType.LOVE);
        CounselEntity counselEntity4 = createCounsel(userEntity, "상담 질문 내용4", CounselType.LOVE);
        CounselEntity counselEntity5 = createCounsel(userEntity, "상담 질문 내용5", CounselType.MENTAL_HEALTH);
        CounselEntity counselEntity6 = createCounsel(userEntity, "상담 질문 내용6", CounselType.LOVE);
        CounselEntity counselEntity7 = createCounsel(userEntity, "상담 질문 내용7", CounselType.LOVE);
        CounselEntity counselEntity8 = createCounsel(userEntity, "상담 질문 내용8", CounselType.LOVE);
        CounselEntity counselEntity9 = createCounsel(userEntity, "상담 질문 내용9", CounselType.LOVE);
        CounselEntity counselEntity10 = createCounsel(userEntity, "상담 질문 내용10", CounselType.LOVE);
        CounselEntity counselEntity11 = createCounsel(userEntity, "상담 질문 내용12", CounselType.LOVE);

        counselRepository.saveAll(List.of(counselEntity1, counselEntity2, counselEntity3, counselEntity4, counselEntity5
                , counselEntity6, counselEntity7, counselEntity8, counselEntity9, counselEntity10, counselEntity11));

        String uniqueValue = socialId + "_" + socialType;
        DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        //when & then
        return List.of(
                DynamicTest.dynamicTest("상담 목록을 조회합니다.", () -> {
                    //given
                    int validPageNumber = 1;
                    //when
                    CounselListResponse result = counselService.findCounsels(uniqueValue, validPageNumber);
                    // then
                    assertThat(result)
                            .extracting("currentPage", "numberPerPage", "totalPage", "totalElements")
                            .containsExactly(1, 10, 2, 11L);
                    assertThat(result.getCounselContent())
                            .hasSize(10)
                            .extracting("date", "counselId", "isAnalysisUsed", "counselType")
                            .containsExactly(
                                    tuple(getDateFormatBy(counselEntity1, datePattern), counselEntity1.getId(), false, counselEntity1.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity2, datePattern), counselEntity2.getId(), false, counselEntity2.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity3, datePattern), counselEntity3.getId(), false, counselEntity3.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity4, datePattern), counselEntity4.getId(), false, counselEntity4.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity5, datePattern), counselEntity5.getId(), false, counselEntity5.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity6, datePattern), counselEntity6.getId(), false, counselEntity6.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity7, datePattern), counselEntity7.getId(), false, counselEntity7.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity8, datePattern), counselEntity8.getId(), false, counselEntity8.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity9, datePattern), counselEntity9.getId(), false, counselEntity9.getCounselType().getDescription()),
                                    tuple(getDateFormatBy(counselEntity10, datePattern), counselEntity10.getId(), false, counselEntity10.getCounselType().getDescription())
                            );
                }),
                DynamicTest.dynamicTest("존재하지 않는 페이지에 접근할 수 없습니다.", () -> {
                    //given
                    int unValidPageNumber = 3;
                    //when & then
                    assertThatThrownBy(() ->
                            counselService.findCounsels(uniqueValue, unValidPageNumber))
                            .isInstanceOf(CounselNotExistPageException.class)
                            .hasMessage("존재하지 않는 페이지입니다.");
                })
        );
    }

    private String getDateFormatBy(CounselEntity counselEntity, DateTimeFormatter datePattern) {
        return counselEntity.getCreatedTime().format(datePattern);
    }


    private AnswerEntity createAnswer() {
        return AnswerEntity.builder()
                .build();
    }

    private AnalysisEntity createAnalysis(UserEntity savedUserEntity) {
        return AnalysisEntity.builder()
                .userEntity(savedUserEntity)
                .build();
    }

    private UserEntity createUser(String socialId, SocialType socialType) {
        return UserEntity.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }

    private CounselEntity createCounsel(UserEntity userEntity, String question, CounselType counselType) {
        return CounselEntity.builder()
                .counselType(counselType)
                .question(question)
                .userEntity(userEntity)
                .build();
    }

}