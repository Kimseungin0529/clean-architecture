package com.project.doongdoong.domain.counsel.dto;

import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.model.CounselType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselCreateRequest {
    private Double feellingScore;

    @NotNull(message = "상담 카테코리는 필수입니다.")
    private CounselType counselType;


    public Counsel toEntity(){
        return Counsel.builder()
                .feellingState(this.feellingScore)
                .counselType(this.counselType)
                .build();

    }
}
