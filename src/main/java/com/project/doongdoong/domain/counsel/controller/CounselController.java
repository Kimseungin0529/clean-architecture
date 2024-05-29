package com.project.doongdoong.domain.counsel.controller;

import com.project.doongdoong.domain.counsel.dto.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.CounselResultResponse;
import com.project.doongdoong.domain.counsel.service.CounselService;
import com.project.doongdoong.global.CurrentUser;
import com.project.doongdoong.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/counsel")
@RequiredArgsConstructor
public class CounselController {

    private final CounselService counselService;

    /**
     * 2. 상담하기(생성)
     * 3. 상담 단일 조회
     * 4. 상담 페이징 조회
     */

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<CounselResultResponse> consult(@CurrentUser String socialId
                                        , @RequestBody CounselCreateRequest request, HttpServletResponse response){

        CounselResultResponse result = counselService.consult(socialId, request);

        URI location = UriComponentsBuilder.fromPath("/api/counsel/{id}")
                .buildAndExpand(result.getCounselId())
                .toUri();

        response.setHeader("Location", location.toString());

        return ApiResponse.of(HttpStatus.CREATED, null, result);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> findCouselContent(@CurrentUser String socialId){
        counselService.findCouselContent();

        return null;
    }

    @GetMapping
    public ApiResponse<?> findConusels(@CurrentUser String socialId){
        counselService.findConusels();

        return null;
    }


}
