package com.project.doongdoong.domain.analysis.controller;

import com.project.doongdoong.domain.analysis.dto.AnalysisCreateResponseDto;
import com.project.doongdoong.domain.analysis.dto.AnaylsisResponseDto;
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
    public ApiResponse<AnaylsisResponseDto> getAnalysis(@PathVariable(name = "id", required = true) Long id){

        return ApiResponse.of(HttpStatus.OK, null, analysisService.getAnalysis(id));
    }

}
