package com.project.doongdoong.domain.voice.adapter.in.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter @NoArgsConstructor
public class VoicesResponseDto {
    private List<VoiceDetailResponseDto> voicesResponse = new ArrayList<>();
}

