package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.counsel.dto.request.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.response.CounselResultResponse;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.domain.counsel.repository.CounselRepository;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.global.dto.response.CounselAiResponse;
import com.project.doongdoong.global.util.WebClientUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
//@Transactional
class CounselServiceImplTest {
    @InjectMocks
    CounselServiceImpl counselService;
    @Mock
    CounselRepository counselRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AnalysisRepository analysisRepository;
    @Mock
    WebClientUtil webClientUtil;

    @Test
    @DisplayName("상담 이후, 상담 결과를 알려줍니다.")
    void consult(){
        //given
        String question = "나의 고민을 해결해줘.";
        String socialId = "123";
        String socialType = "APPLE";
        String uniqueValue = socialId + "_" + socialType;
        CounselType counselType = Arrays.stream(CounselType.values()).findAny().get();

        User user = createUserMethod(socialId, "abc@naver.com", "닉네임", SocialType.APPLE);
        CounselCreateRequest request = new CounselCreateRequest(counselType.getCotent(), question);

        when(userRepository.findBySocialTypeAndSocialId(SocialType.APPLE, socialId)).thenReturn(Optional.of(user));
        when(webClientUtil.callConsult(new HashMap<>())).thenReturn(new CounselAiResponse("", ""));
        when(counselRepository.save(any(Counsel.class))).thenAnswer(invocation -> {
            Counsel savedCounsel = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedCounsel, "id", 1L); // 여기서 ID를 설정합니다
            return savedCounsel;
        });

        //when
        CounselResultResponse result = counselService.consult(uniqueValue, request);
        //then
        assertThat(result)
                .extracting("counselId", "counselContent", "imageUrl")
                .contains(Long.class, String.class, String.class);

    }

    @Test
    @DisplayName("기존 분석 내용과 함께 상담을 진행한다.")
    void consultWithAnalysisContents(){
        //given

        User user = createUserMethod("1", "fdf@naver.com","진이", SocialType.APPLE);
        //Counsel counsel = createCounselMethod(81.6, CounselType.);

     /*   Counsel savedCounsel = counselRepository.save(counsel);

        CounselCreateRequest request = CounselCreateRequest.builder()
                .analysisId(1L)
                .build();

        when(userRepository.findBySocialTypeAndSocialId(user.getSocialType(), user.getSocialId()))
                .thenReturn(Optional.of(user));
        when(analysisRepository.findById(any(Long.class))).thenAnswer(invocation -> {
            Analysis savedAnalysis = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedAnalysis, "id", 1L); // 여기서 ID를 설정합니다
            return savedAnalysis;
        });*/
       /* when(counselRepository.save())

        boolean option = true;
        //when
        CounselResultResponse result = counselService.consult(savedUser.getSocialId(), savedCounsel.getId(), );
        //then
        assertThat(result).isNotNull();
        assertThat(result.getCounselResult()).isNotBlank();*/
    }

    private static User createUserMethod(String socialId, String email, String nickname, SocialType socialType) {
        return User.builder()
                .socialId(socialId)
                .email(email)
                .nickname(nickname)
                .socialType(socialType)
                .build();
    }

    private static Counsel createCounselMethod(Double feellingState, CounselType counselType) {
        Counsel counsel = Counsel.builder()
                .counselType(counselType)
                .build();
        return counsel;
    }

}