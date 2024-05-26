package com.project.doongdoong.domain.counsel.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.project.doongdoong.domain.counsel.exception.CounselTypeInvalidException;
import com.project.doongdoong.global.exception.CustomException;
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

    @JsonCreator
    public static CounselType from(String value) {
        for (CounselType type : CounselType.values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        throw new CounselTypeInvalidException(value);
    }

}
