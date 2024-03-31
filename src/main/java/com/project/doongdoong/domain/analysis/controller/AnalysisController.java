package com.project.doongdoong.domain.analysis.controller;

import com.project.doongdoong.domain.analysis.service.AnalysisService;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.global.CurrentUser;
import com.project.doongdoong.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ApiResponse<?> createAnaysis(@CurrentUser User user){

        return null;
    }

}
