package com.project.doongdoong.domain.analysis.service;

import com.project.doongdoong.domain.analysis.repository.AnalsisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private AnalsisRepository analsisRepository;
}
