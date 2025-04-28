package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.model.CounselCacheKey;
import com.project.doongdoong.domain.counsel.model.CounselRank;
import com.project.doongdoong.domain.counsel.model.CounselType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounselStatisticsServiceImpl implements CounselStatisticsService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int PLUS = 1, WEEKS = 7;

    @Override
    public void incrementCategoryCount(CounselType counselType) {
        String key = CounselCacheKey.generateTotalKey();
        redisTemplate.opsForHash().increment(key, counselType.name(), PLUS);

        String dailyKey = CounselCacheKey.generateDailyKey(LocalDate.now());
        redisTemplate.opsForZSet().incrementScore(dailyKey, counselType.name(), PLUS);
        redisTemplate.expire(dailyKey, WEEKS, TimeUnit.DAYS);
    }

    @Override
    public List<CounselRank> getCombinedRanking() {
        Set<ZSetOperations.TypedTuple<Object>> cached = redisTemplate.opsForZSet()
                .rangeWithScores(CounselCacheKey.generateTotalKey(), 0, -1);

        if (cached != null && !cached.isEmpty()) {
            return cached.stream()
                    .map(entry -> CounselRank.of(entry.getValue().toString(), entry.getScore()))
                    .toList();
        }

        // 전체 계산
        Map<String, Long> totalCountMap = redisTemplate.opsForHash().entries(CounselCacheKey.generateTotalKey())
                .entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> Long.parseLong(entry.getValue().toString())
                ));

        // 일주일 치 계산
        List<String> keys = CounselCacheKey.generateKeysBefore(LocalDate.now(), WEEKS);
        redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), CounselCacheKey.COMBINED_COUNT.name());
        Set<ZSetOperations.TypedTuple<Object>> entries = redisTemplate.opsForZSet().rangeWithScores(CounselCacheKey.COMBINED_COUNT.name(), 0, -1);


        return entries.stream()
                .map(entry -> CounselRank.of(entry.getValue().toString(), entry.getScore()))
                .toList();
    }

}
