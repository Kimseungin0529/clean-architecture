package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.IntegrationSupportTest;
import com.project.doongdoong.domain.analysis.dto.response.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AnalysisServiceImpTest extends IntegrationSupportTest {

    @Autowired
    AnalysisService analysisService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    VoiceRepository voiceRepository;

    @Test
    @DisplayName("질문 목록에 대한 접근 url, 내용 등 분석에 사용할 정보를 생성한다.")
    void createAnalysis(){
        //given
        User user = createUser("socialId", SocialType.APPLE);
        User savedUser = userRepository.save(user);
        String uniqueValue = savedUser.getSocialId() + "_" + savedUser.getSocialType().getText();

        for(QuestionContent questionContent : QuestionContent.values()){
            Voice voice = Voice.initVoiceContentBuilder()
                    .originName(questionContent.getText() +"_voice.mp3")
                    .questionContent(questionContent)
                    .build();
            voice.changeAccessUrl("임의의 접근 url 주소");
            voiceRepository.save(voice);
        }

        int analysisRelatedSize = 4;
        List<String> allQuestionTexts = Arrays.stream(QuestionContent.values())
                .map(QuestionContent::getText)
                .collect(Collectors.toList());

        //when
        AnalysisCreateResponseDto responseDto = analysisService.createAnalysis(uniqueValue);

        //then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getAnalysisId()).isExactlyInstanceOf(Long.class);
        assertThat(responseDto.getQuestionTexts())
                .hasSize(analysisRelatedSize)
                .allMatch(allQuestionTexts::contains);
        assertThat(responseDto.getAccessUrls()).hasSize(analysisRelatedSize);
    }

    private static User createUser(String socialId, SocialType socialType) {
        return User.builder()
                .socialId(socialId)
                .socialType(socialType)
                .build();
    }


}