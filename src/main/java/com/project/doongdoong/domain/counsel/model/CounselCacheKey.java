package com.project.doongdoong.domain.counsel.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CounselCacheKey {
    TOTAL_COUNT("counseling:count:total:%s"),
    WEEKLY_COUNT("counseling:count:week:%s");

    private final String pattern;

}
