package com.project.doongdoong.domain.counsel.domain;

public record CounselRank(
        String counselType,
        double count
) {
    public static CounselRank of(String counselType, double count) {
        return new CounselRank(counselType, count);
    }

}
