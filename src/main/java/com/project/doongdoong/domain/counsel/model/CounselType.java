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

    LOVE("연애"),
    JOB("취업진로"),
    MENTAL_HEALTH("정신건강"),
    RELATIONSHIP("대인관계"),
    FAMILY("가족");

    private final String cotent;

    public static CounselType from(String value) {

        return Arrays.stream(CounselType.values())
                .filter(i -> i.getCotent().equals(value))
                .findAny()
                .orElseThrow(() -> new CounselTypeInvalidException(value));
    }

}
