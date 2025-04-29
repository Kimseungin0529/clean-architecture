package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.dto.CounselRankList;
import com.project.doongdoong.domain.counsel.model.CounselCacheKey;
import com.project.doongdoong.domain.counsel.model.CounselRank;
import com.project.doongdoong.domain.counsel.model.CounselType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CounselStatisticsServiceImpl implements CounselStatisticsService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int ONE = 1, WEEKS = 7;

    @Override
    public void incrementTypeCount(CounselType counselType) {
        String key = CounselCacheKey.generateTotalKey();
        redisTemplate.opsForZSet().incrementScore(key, counselType.name(), ONE);

        String dailyKey = CounselCacheKey.generateDailyKey(LocalDate.now());
        redisTemplate.opsForZSet().incrementScore(dailyKey, counselType.name(), ONE);
        redisTemplate.expire(dailyKey, WEEKS, TimeUnit.DAYS);
    }

    @Override
    public CounselRankList getCombinedRanking() {

        Set<ZSetOperations.TypedTuple<Object>> totalRankingEntries = redisTemplate.opsForZSet().rangeWithScores(CounselCacheKey.generateTotalKey(), 0, -1);
        List<CounselRank> totalRanking = totalRankingEntries.stream()
                .map(entry -> CounselRank.of(entry.getValue().toString(), entry.getScore()))
                .toList();

        if (Boolean.FALSE.equals(redisTemplate.hasKey(CounselCacheKey.WEEKS_COUNT.getPattern()))) {
            // 일주일 요소 계산(만료 기한은 1일로 해당 요소에 대한 키가 없다면 다시 저장)
            List<String> keys = CounselCacheKey.generateKeysBefore(LocalDate.now(), WEEKS);
            redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), CounselCacheKey.WEEKS_COUNT.name());
            redisTemplate.expire(CounselCacheKey.WEEKS_COUNT.getPattern(), ONE, TimeUnit.DAYS);
        }


        Set<ZSetOperations.TypedTuple<Object>> weeksEntries = redisTemplate.opsForZSet().rangeWithScores(CounselCacheKey.WEEKS_COUNT.getPattern(), 0, -1);
        List<CounselRank> weeksRanking = weeksEntries.stream()
                .map(entry -> CounselRank.of(entry.getValue().toString(), entry.getScore() != null ? entry.getScore() : 0.0))
                .toList();


        return CounselRankList.of(totalRanking, weeksRanking);
    }

}
