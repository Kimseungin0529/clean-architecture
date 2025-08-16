package com.project.doongdoong.domain.voice.adapter.in.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoiceDetailResponseDto {
    private String accessUrl;
    private Long voiceId;

    public static VoiceDetailResponseDto of(String accessUrl, Long voiceId) {
        return new VoiceDetailResponseDto(accessUrl, voiceId);
    }

    private VoiceDetailResponseDto(String accessUrl, Long voiceId) {
        this.accessUrl = accessUrl;
        this.voiceId = voiceId;
    }
}



