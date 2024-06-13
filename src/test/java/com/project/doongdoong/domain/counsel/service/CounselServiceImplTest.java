package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.domain.counsel.repository.CounselRepository;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CounselServiceImplTest {
    @Autowired
    CounselService counselService;
    @MockBean
    CounselRepository counselRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    AnalysisRepository analysisRepository;

    @Test
    @DisplayName("상담 이후, 상담 결과를 알려줍니다.")
    void createCounsel(){
        //given
        CounselType counselType = Arrays.stream(CounselType.values()).findAny().get();
        User user = createUserMethod("1", "abc@naver.com", "진진2", SocialType.APPLE);
        //CounselCreateRequest request = new CounselCreateRequest(100.0, counselType.toString());

        when(userRepository.findBySocialTypeAndSocialId(user.getSocialType(),user.getSocialId())).thenReturn(Optional.of(user));
        when(counselRepository.save(any(Counsel.class))).thenAnswer(invocation -> {
            Counsel savedCounsel = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedCounsel, "id", 1L); // 여기서 ID를 설정합니다
            return savedCounsel;
        });

        //when
        Long createdValue = counselService.createCounsel(user.getSocialId(), request);
        //then
        assertThat(createdValue).isNotNull();
    }

    @Test
    @DisplayName("기존 분석 내용과 함께 상담을 진행한다.")
    void consult(){
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