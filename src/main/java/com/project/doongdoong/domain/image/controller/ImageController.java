package com.project.doongdoong.domain.image.controller;

import com.project.doongdoong.domain.image.dto.ImageSaveDto;
import com.project.doongdoong.domain.image.service.ImageService;
import com.project.doongdoong.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/image")
    public ApiResponse<List<String>> saveImage(@ModelAttribute @Valid ImageSaveDto imageSaveDto) {
        log.info("이미지 컨트롤러 로직 시작");
        return ApiResponse.of(HttpStatus.OK, null, imageService.saveImages(imageSaveDto));
    }


}
