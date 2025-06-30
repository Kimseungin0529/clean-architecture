package com.project.doongdoong.domain.counsel.application.port.in;

import com.project.doongdoong.domain.counsel.dto.CounselRankList;
import com.project.doongdoong.domain.counsel.model.CounselType;

public interface CounselStatisticsService {
    void incrementTypeCount(CounselType counselType);

    CounselRankList getCombinedRanking();
}
