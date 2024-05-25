package com.project.doongdoong.domain.consultation.model;

import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counsel extends BaseEntity {

    @Id
    @Column(name = "counsel_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long feelingState;

    /**
     * 주고 받는 텍스트 값이 필요하다.
     */
    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QAPair> qaPairs = new ArrayList<>();

    @Builder
    public Counsel(Long feelingState) {
        this.feelingState = feelingState;
    }
}
