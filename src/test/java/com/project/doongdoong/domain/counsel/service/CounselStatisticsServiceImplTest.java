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
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clearRedisKeys() {
        redisTemplate.delete(CounselCacheKey.generateTotalKey(CounselType.JOB));
        redisTemplate.delete(CounselCacheKey.generateDailyKey(LocalDate.now(), CounselType.JOB));

        redisTemplate.delete(CounselCacheKey.generateTotalKey(CounselType.LOVE));
        redisTemplate.delete(CounselCacheKey.generateDailyKey(LocalDate.of(2020, 1, 15), CounselType.LOVE));
        redisTemplate.delete(CounselCacheKey.generateDailyKey(LocalDate.now(), CounselType.LOVE));
    }


    @DisplayName("카테고리 별 주제 개수 통계 시나리오")
    @TestFactory
    Collection<DynamicTest> incrementCategoryCount() {
        // given
        String jobTotalKey = CounselCacheKey.generateTotalKey(CounselType.JOB);
        String loveTotalKey = CounselCacheKey.generateTotalKey(CounselType.LOVE);
        String pastLoveDailyKey = CounselCacheKey.generateDailyKey(LocalDate.of(2020, 1, 15), CounselType.LOVE);

        redisTemplate.opsForValue().set(jobTotalKey, "1");
        redisTemplate.opsForValue().set(loveTotalKey, "5");
        redisTemplate.opsForValue().set(pastLoveDailyKey, "3");

        // when & then
        return List.of(
                DynamicTest.dynamicTest("데일리 진로 유형 키가 없는 경우에 진로 상담 유형 항목 수를 증가시킵니다.", () -> {
                    //given
                    CounselType counselType = CounselType.JOB;
                    String jobDailyKey = CounselCacheKey.generateDailyKey(LocalDate.now(), CounselType.JOB);
                    // when
                    counselStatisticsService.incrementCategoryCount(counselType);
                    // then
                    assertThat(redisTemplate.opsForValue().get(jobDailyKey)).isEqualTo("1");
                    assertThat(redisTemplate.opsForValue().get(jobTotalKey)).isEqualTo("2");
                }),
                DynamicTest.dynamicTest("연애 유형 키가 전부 있는 경우에 연애 상담 유형 항목 수를 증가시킵니다.", () -> {
                            //given
                            String loveDailyKey = CounselCacheKey.generateDailyKey(LocalDate.now(), CounselType.LOVE);
                            // when
                            counselStatisticsService.incrementCategoryCount(CounselType.LOVE);
                            // then
                            assertThat(redisTemplate.opsForValue().get(loveDailyKey)).isEqualTo("1");
                            assertThat(redisTemplate.opsForValue().get(loveTotalKey)).isEqualTo("6");
                        }
                ));


    }

}