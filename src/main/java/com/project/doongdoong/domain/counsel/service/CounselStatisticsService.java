package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.model.CounselType;

import java.util.List;
import java.util.Map;

public interface CounselStatisticsService {
    void incrementCategoryCount(CounselType counselType);

    Map<String, List<String>> getCombinedRanking();
}
