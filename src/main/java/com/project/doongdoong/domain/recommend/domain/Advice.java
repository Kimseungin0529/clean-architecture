package com.project.doongdoong.domain.recommend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Advice {
    A("슬픔", "넌 세상에서 가장 소중해.", "미키마우스"); // 임시 처리

    private final String emotion;
    private final String AdviceContent;
    private final String adviser;
}
