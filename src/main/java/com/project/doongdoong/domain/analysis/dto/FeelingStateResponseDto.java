package com.project.doongdoong.domain.analysis.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeelingStateResponseDto {

    private String date;
    private Double avgFeelingState;

    public FeelingStateResponseDto(String date, Double avgFeelingState) {
        this.date = date;
        this.avgFeelingState = avgFeelingState;
    }
}
