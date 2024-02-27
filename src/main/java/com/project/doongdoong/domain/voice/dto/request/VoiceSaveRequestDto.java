package com.project.doongdoong.domain.voice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter // setter가 있어야 dto에 이미지가 들어감.
@NoArgsConstructor
public class VoiceSaveRequestDto {
    @Schema(description = "List of voice file to upload.")
    //@Size(min = 1, max = 3, message = "이미지는 최소 1개에서 최대 3개까지 저장할 수 있습니다.")
    private List<MultipartFile> voices;

    @Builder
    public VoiceSaveRequestDto(List<MultipartFile> voices) {
        this.voices = voices;
    }

}
