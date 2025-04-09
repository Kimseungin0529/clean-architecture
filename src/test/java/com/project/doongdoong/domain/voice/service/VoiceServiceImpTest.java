package com.project.doongdoong.domain.voice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.project.doongdoong.domain.voice.dto.response.VoiceDetailResponseDto;
import com.project.doongdoong.domain.voice.model.Voice;
import com.project.doongdoong.domain.voice.repository.VoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class VoiceServiceImpTest {

    @Mock
    AmazonS3Client amazonS3Client;

    @Mock
    VoiceRepository voiceRepository;

    @InjectMocks
    VoiceServiceImp voiceService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(voiceService, "bucketName", "test-bucket");
    }

    @DisplayName("음성 파일을 저장합니다.")
    @Test
    void saveVoice() throws MalformedURLException {
        // given
        String originalFilename = "test.mp3";
        MockMultipartFile mockFile = new MockMultipartFile("voice", originalFilename, "audio/mpeg", "test-audio".getBytes());

        String expectedUrl = "https://s3.amazonaws.com/bucket/voice/test.mp3";

        Voice voice = Voice.commonBuilder()
                .originName(originalFilename)
                .build();

        given(amazonS3Client.getUrl(anyString(), anyString()))
                .willReturn(new URL(expectedUrl));
        given(amazonS3Client.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class)))
                .willReturn(new PutObjectResult());
        given(voiceRepository.save(any(Voice.class)))
                .willReturn(voice);
        // when
        VoiceDetailResponseDto result = voiceService.saveVoice(mockFile);

        // then
        assertThat(result.getAccessUrl()).isEqualTo(expectedUrl);

        verify(voiceRepository).save(any(Voice.class));
        verify(amazonS3Client).putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class));
    }

}