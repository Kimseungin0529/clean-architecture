package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.model.CounselCacheKey;
import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.domain.counsel.repository.CounselCachingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CounselStatisticsServiceImpl implements CounselStatisticsService {

    private final CounselCachingRepository counselCachingRepository;

    private static final int PLUS = 1;

    @Override
    public void incrementCategoryCount(CounselType counselType) {
        String totalKey = CounselCacheKey.generateTotalKey(counselType);
        counselCachingRepository.incrementValue(totalKey, PLUS);

        String dailyKey = CounselCacheKey.generateDailyKey(LocalDate.now(), counselType);
        counselCachingRepository.incrementValue(dailyKey, PLUS);
    }

    @Override
    public Map<String, List<String>> getCombinedRanking() {

        return null;
    }

}
