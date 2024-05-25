package com.project.doongdoong.domain.consultation.controller;

import com.project.doongdoong.domain.consultation.service.CounselService;
import com.project.doongdoong.global.CurrentUser;
import com.project.doongdoong.global.common.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/counsel")
@RequiredArgsConstructor
public class CounselController {

    private final CounselService counselService;

    /**
     * 1. 상담 생성
     * 2. 상담 단일 조회
     * 3. 상담 페이징 조회
     */

    @PostMapping
    public ApiResponse<?> createCounsel(@CurrentUser String socialId){
        counselService.createCounsel();

        return ApiResponse.of(HttpStatus.CREATED, null, null);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> consult(@CurrentUser String socialId){
        counselService.consult();

        return null;
    }

    @GetMapping("/{id}")
    public ApiResponse<?> findCouselContent(@CurrentUser String socialId){
        counselService.findCouselContent();

        return null;
    }

    @GetMapping("/{id}")
    public ApiResponse<?> findConusels(@CurrentUser String socialId){
        counselService.findConusels();

        return null;
    }


}
