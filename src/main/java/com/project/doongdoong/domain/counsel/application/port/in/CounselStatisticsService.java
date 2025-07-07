package com.project.doongdoong.domain.counsel.application.port.in;

import com.project.doongdoong.domain.counsel.dto.CounselRankList;
import com.project.doongdoong.domain.counsel.domain.CounselType;

public interface CounselStatisticsService {
    void incrementTypeCount(CounselType counselType);

    CounselRankList getCombinedRanking();
}
