package com.project.doongdoong.domain.analysis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisEmotionRequestDto {
    private File file;

    public AnalysisEmotionRequestDto(File file) {
        this.file = file;
    }

    public static AnalysisEmotionRequestDto of(File file){
        return new AnalysisEmotionRequestDto(file);
    }
}
