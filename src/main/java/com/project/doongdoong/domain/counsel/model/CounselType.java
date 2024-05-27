package com.project.doongdoong.domain.counsel.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.project.doongdoong.domain.counsel.exception.CounselTypeInvalidException;
import com.project.doongdoong.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
        System.out.println(FAMILY.name());

        return Arrays.stream(CounselType.values())
                .filter(i -> i.name().equals(value))
                .findAny()
                .orElseThrow(() -> new CounselTypeInvalidException(value));
    }

}
