package com.project.doongdoong.domain.voice.dto.response;

import com.project.doongdoong.domain.image.dto.response.ImageDetailResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter @NoArgsConstructor
public class VoicesResponseDto {
    private List<VoiceDetailResponseDto> voicesResponse = new ArrayList<>();
}

