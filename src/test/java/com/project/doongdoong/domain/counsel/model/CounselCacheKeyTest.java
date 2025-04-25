package com.project.doongdoong.domain.counsel.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CounselCacheKeyTest {

    @DisplayName("날짜와 상담 유형에 대한 상담 키를 발급합니다.")
    @Test
    void generateDailyKey() {
        // given
        LocalDate localDate = LocalDate.of(2020, 1, 1);
        CounselType counselType = CounselType.JOB;
        // when
        String result = CounselCacheKey.generateDailyKey(localDate, counselType);
        // then
        assertThat(result)
                .isEqualTo("counseling:count:" + localDate + ":" + counselType.name());
    }

    @DisplayName("상담 유형에 대한 상담 키를 발급합니다.")
    @Test
    void generateTotalKey() {
        // given
        CounselType counselType = CounselType.LOVE;
        // when
        String result = CounselCacheKey.generateTotalKey(counselType);
        // then
        assertThat(result)
                .isEqualTo("counseling:count:total:" + counselType.name());
    }


}