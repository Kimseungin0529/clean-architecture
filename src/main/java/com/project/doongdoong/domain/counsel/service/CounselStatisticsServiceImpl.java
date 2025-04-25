package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.domain.counsel.repository.CounselCachingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CounselStatisticsServiceImpl implements CounselStatisticsService {

    private final CounselCachingRepository counselCachingRepository;

    private static final int PLUS = 1, WEEKS = 7;

    @Override
    public void incrementCategoryCount(CounselType counselType) {

    }

    @Override
    public Map<String, List<String>> getCombinedRanking() {

        return null;
    }

}
