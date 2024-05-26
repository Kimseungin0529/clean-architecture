package com.project.doongdoong.domain.counsel.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CounselType {

    FAMILY("가족"),
    LOVE("사랑"),
    FRIEND("친구"),
    JOB("직장"),
    ETC("기타");

    private final String cotent;

}
