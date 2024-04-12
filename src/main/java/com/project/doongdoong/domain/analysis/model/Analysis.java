package com.project.doongdoong.domain.analysis.model;

import com.project.doongdoong.domain.answer.model.Answer;
import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter                                            // pubilc 대신 protected? 무분별한 생성을 막기 위해서 라는데 무분별하게 생성할 일이 있을까?
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 지연 로딩으로 인해 프록시 객체(상속)하므로 private 대신 protected를 사용
public class Analysis extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "analysis_id")
    private Long id;

    @Column(updatable = false)
    private double feelingState;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = ALL ,orphanRemoval = true, mappedBy = "analysis")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(cascade = ALL ,orphanRemoval = true, mappedBy = "analysis")
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Analysis(User user, List<Question> questions){
        this.feelingState = 0;
        this.questions = questions;
        this.user = user;
    }

}
