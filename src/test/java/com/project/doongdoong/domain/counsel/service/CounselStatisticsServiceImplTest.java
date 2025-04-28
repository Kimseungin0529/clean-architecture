package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.counsel.model.CounselCacheKey;
import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.module.IntegrationSupportTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CounselStatisticsServiceImplTest extends IntegrationSupportTest {

    @Autowired
    CounselStatisticsService counselStatisticsService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }


    @DisplayName("상담 주제 별 개수 통계 시나리오")
    @TestFactory
    Collection<DynamicTest> incrementCategoryCount() {
        // given
        String totalKey = CounselCacheKey.generateTotalKey();
        LocalDate now = LocalDate.now();
        String dailyKey = CounselCacheKey.generateDailyKey(now);

        CounselType counselTypeLove = CounselType.LOVE;
        redisTemplate.opsForHash().increment(totalKey, counselTypeLove.name(), 21);
        redisTemplate.opsForZSet().add(dailyKey, counselTypeLove.name(), 3);
        int days = 0;
        for (int i = 0; i < 7; i++) {
            days += i;
            redisTemplate.opsForZSet().add(CounselCacheKey.generateDailyKey(now.minusDays(i)), counselTypeLove.name(), i);
        }
        int finalDays = days;

        // when & then
        return List.of(
                DynamicTest.dynamicTest("상담 유형이 JOB 인 관련 값을 증가시킵니다.", () -> {
                    //given
                    CounselType counselType = CounselType.JOB;
                    // when
                    counselStatisticsService.incrementCategoryCount(counselType);
                    // then

                    assertThat(redisTemplate.opsForHash().get(totalKey, counselTypeLove.name()))
                            .isEqualTo(String.valueOf(finalDays));
                    assertThat(redisTemplate.opsForHash().get(totalKey, counselType.name()))
                            .isEqualTo("1");
                    assertThat(redisTemplate.opsForZSet().score(dailyKey, counselType.name()))
                            .isEqualTo(1.0);
                }),
                DynamicTest.dynamicTest("상담 유형이 LOVE 인 관련 값을 증가시킵니다", () -> {
                    // when
                    counselStatisticsService.incrementCategoryCount(counselTypeLove);
                    // then
                    assertThat(redisTemplate.opsForHash().get(totalKey, counselTypeLove.name()))
                            .isEqualTo(String.valueOf(finalDays + 1));
                    assertThat(redisTemplate.opsForZSet().score(dailyKey, counselTypeLove.name()))
                            .isEqualTo(1.0);
                })
        );


    }

}