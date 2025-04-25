package com.project.doongdoong.domain.counsel.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public enum CounselCacheKey {
    TOTAL_COUNT("counseling:count:total:%s"),
    DAY_COUNT("counseling:count:%s:%s");

    private final String pattern;

    public static String generateDailyKey(LocalDate localDate, CounselType counselType) {
        return String.format(DAY_COUNT.pattern, localDate.toString(), counselType.name());
    }

    public static String generateTotalKey(CounselType counselType) {
        return String.format(TOTAL_COUNT.pattern, counselType.name());
    }
}
