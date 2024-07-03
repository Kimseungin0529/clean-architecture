package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.analysis.exception.AnalysisAccessDeny;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.counsel.dto.request.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.response.CounselResultResponse;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.domain.counsel.repository.CounselRepository;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.global.dto.response.CounselAiResponse;
import com.project.doongdoong.global.util.WebClientUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CounselServiceImplTest {
    @Autowired CounselServiceImpl counselService;
    @SpyBean AnalysisRepository analysisRepository;
    @Autowired CounselRepository counselRepository;
    @Autowired UserRepository userRepository;
    @MockBean WebClientUtil webClientUtil;

    @Test
    @DisplayName("회원의 질문에 대한 상담 답변을 제공한다.")
    void consult(){
        //given
        String socialId = "123456";
        User user = User.builder()
                .socialId(socialId)
                .socialType(SocialType.APPLE)
                .build();
        User savedUser = userRepository.save(user);

        CounselCreateRequest request = new CounselCreateRequest(
                "취업진로",
                "나는 취업에 대한 고민이 있어. 내가 개발자가 될 수 있을까? 어떤 노력이 필요해?"
        );
        CounselAiResponse mockResponse = new CounselAiResponse(
                "개발자가 되기 위해서는 꾸준한 학습과 프로젝트 경험이 필요합니다.",
                "http://example.com/image.jpg"
        );
        String uniqueValue = savedUser.getSocialId() + "_" + savedUser.getSocialType().getText();

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
    void consultWithAnalysis(){
        //given
        String socialId = "123456";
        User user = User.builder()
                .socialId(socialId)
                .socialType(SocialType.APPLE)
                .build();
        User savedUser = userRepository.save(user);

        Analysis analysis = Analysis.builder()
                .user(savedUser)
                .build();
        Analysis savedAnalysis = analysisRepository.save(analysis);

        CounselCreateRequest request = new CounselCreateRequest(
                "취업진로",
                "나는 취업에 대한 고민이 있어. 내가 개발자가 될 수 있을까? 어떤 노력이 필요해?"
        );
        CounselAiResponse mockResponse = new CounselAiResponse(
                "개발자가 되기 위해서는 꾸준한 학습과 프로젝트 경험이 필요합니다.",
                "http://example.com/image.jpg"
        );
        String uniqueValue = savedUser.getSocialId() + "_" + savedUser.getSocialType().getText();

        doReturn(Optional.of(savedAnalysis)) // 분석 설정이 너무 거대하므로 해당 로직만 스터빙 처리
                .when(analysisRepository).findByUserAndId(savedUser, savedAnalysis.getId());
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
}