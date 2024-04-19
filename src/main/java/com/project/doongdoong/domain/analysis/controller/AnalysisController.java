package com.project.doongdoong.domain.analysis.controller;

import com.project.doongdoong.domain.analysis.dto.request.AnalysisEmotionRequestDto;
import com.project.doongdoong.domain.analysis.dto.response.*;
import com.project.doongdoong.domain.analysis.service.AnalysisService;
import com.project.doongdoong.global.CurrentUser;
import com.project.doongdoong.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ApiResponse<AnalysisCreateResponseDto> createAnaysis(@CurrentUser String uniqueValue){

        return ApiResponse.of(HttpStatus.OK, null, analysisService.createAnalysis(uniqueValue));
    }


    @GetMapping("{id}")
    public ApiResponse<AnalysisDetailResponse> getAnalysis(@PathVariable(name = "id", required = true) Long id){

        return ApiResponse.of(HttpStatus.OK, null, analysisService.getAnalysis(id));
    }

    @GetMapping
    public ApiResponse<AnaylsisListResponseDto> getAnalyses(@CurrentUser String uniqueValue
            , @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber){
        System.out.println("시작");
        pageNumber -= 1; // 페이징은 0번부터이므로 1페이지로 표시

        return ApiResponse.of(HttpStatus.OK, null, analysisService.getAnalysisList(uniqueValue ,pageNumber));
    }

    @GetMapping("/week")
    public ApiResponse<FeelingStateResponseListDto> getAnalysesGroupByDay(@CurrentUser String uniqueValue){

        return ApiResponse.of(HttpStatus.OK, null, analysisService.getAnalysisListGroupByDay(uniqueValue));
    }

    @PostMapping("/{id}")
    public ApiResponse<FellingStateCreateResponse> analyzeEmotion(@PathVariable("id") Long analysisId, @RequestBody AnalysisEmotionRequestDto dto){

        return ApiResponse.of(HttpStatus.OK, null, analysisService.analyzeEmotion(analysisId, dto));
    }

}
