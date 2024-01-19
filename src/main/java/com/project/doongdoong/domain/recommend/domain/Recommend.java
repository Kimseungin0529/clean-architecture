package com.project.doongdoong.domain.recommend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendId;

    @Enumerated(value = EnumType.STRING)
    private Sing sing;

    @Enumerated(value = EnumType.STRING)
    private Advice advice;
}
