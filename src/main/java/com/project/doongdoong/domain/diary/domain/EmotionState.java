package com.project.doongdoong.domain.diary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmotionState {

    HAPPY("행복"),
    NORMAL("보통"),
    SAD("슬픔"),
    ANGER("화남"),
    WORRY("걱정");

    private final String value;
}
