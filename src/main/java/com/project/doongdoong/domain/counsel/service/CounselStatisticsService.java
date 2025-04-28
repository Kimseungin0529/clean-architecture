package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.model.CounselRank;
import com.project.doongdoong.domain.counsel.model.CounselType;

import java.util.List;

public interface CounselStatisticsService {
    void incrementCategoryCount(CounselType counselType);

    List<CounselRank> getCombinedRanking();
}
