package com.project.doongdoong.domain.analysis.adapter.in.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class FeelingStateResponseDto {

    private String date;
    private Double avgFeelingState;

    public FeelingStateResponseDto(String date, Double avgFeelingState) {
        this.date = date;
        this.avgFeelingState = avgFeelingState;
    }


}
