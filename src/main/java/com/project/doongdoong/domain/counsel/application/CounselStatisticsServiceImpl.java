package com.project.doongdoong.domain.counsel.application;

import com.project.doongdoong.domain.counsel.application.port.in.CounselStatisticsService;
import com.project.doongdoong.domain.counsel.dto.CounselRankList;
import com.project.doongdoong.domain.counsel.domain.CounselRank;
import com.project.doongdoong.domain.counsel.domain.CounselType;
import com.project.doongdoong.global.util.CounselRankingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CounselStatisticsServiceImpl implements CounselStatisticsService {

    private final CounselRankingCache rankingCache;

    @Override
    public void incrementTypeCount(CounselType counselType) {
        rankingCache.incrementTodayCount(counselType);
        rankingCache.incrementTotalCount(counselType);
    }

    @Override
    public CounselRankList getCombinedRanking() {
        List<CounselRank> totalRanking = rankingCache.getTotalRanking();
        List<CounselRank> weeksRanking = rankingCache.getWeeksRanking();
        return CounselRankList.of(totalRanking, weeksRanking);
    }

}
