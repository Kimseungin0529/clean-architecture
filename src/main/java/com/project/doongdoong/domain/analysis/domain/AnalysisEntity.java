package com.project.doongdoong.domain.analysis.domain;

import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.domain.counsel.domain.CounselEntity;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.user.domain.User;
import com.project.doongdoong.domain.user.domain.UserEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "analysis")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "analysis_id")
    private Long id;

    private double feelingState;

    private LocalDate analyzeTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "analysis")
    private List<QuestionEntity> questions = new ArrayList<>();

    @OneToMany(cascade = ALL, orphanRemoval = true, mappedBy = "analysis")
    private List<AnswerEntity> answers = new ArrayList<>();

    @OneToOne(fetch = LAZY, mappedBy = "analysis")
    private CounselEntity counsel;

    private static final int MAX_ANSWER_COUNT = 4;

    public static AnalysisEntity of(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .questionEntities(questionEntities)
                .build();
    }

    @Builder
    public AnalysisEntity(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        this.feelingState = 0;
        this.questions = questionEntities;
        this.user = userEntity;
    }

    public static AnalysisEntity fromModel(Analysis analysis) {
        return AnalysisEntity.builder()
                .userEntity(UserEntity.fromModel(analysis.getUser()))
                .questionEntities(analysis.getQuestions()
                        .stream()
                        .map(question -> QuestionEntity.fromModel(question))
                        .toList())
                .build();
    }

    public boolean hasAllAnswer() {
        return this.answers.size() == MAX_ANSWER_COUNT;
    }

    public boolean isMissingAnswers() {
        return !hasAllAnswer();
    }


    public void changeFeelingStateAndAnalyzeTime(double feelingState, LocalDate analyzeTime) {
        this.feelingState = feelingState;
        this.analyzeTime = analyzeTime;
    }

    public boolean equalsAnalyzeTimeTo(LocalDate time) {
        if (this.analyzeTime == null || time == null) {
            return false;
        }
        return time.equals(this.analyzeTime);
    }

    public boolean isAlreadyAnalyzed() {
        return this.analyzeTime != null;
    }

    public void addUser(UserEntity user) {
        this.user = user;
        user.getAnalysisList().add(this);
    }

    public Analysis toModel() {
        return Analysis.of(
                id, feelingState, analyzeTime,
                user.toModel(),
                questions.stream().map(QuestionEntity::toModel).toList()
        );
    }
}
