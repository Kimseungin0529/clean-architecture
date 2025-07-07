package com.project.doongdoong.domain.question.domain;

import com.project.doongdoong.domain.analysis.domain.AnalysisEntity;
import com.project.doongdoong.domain.answer.domain.AnswerEntity;
import com.project.doongdoong.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "question_id")
    private Long id;


    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private QuestionContent questionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private AnalysisEntity analysis;

    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "answer_id")
    private AnswerEntity answer;

    private QuestionEntity(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public static QuestionEntity of(QuestionContent questionContent) {
        return new QuestionEntity(questionContent);
    }

    public void connectAnalysis(AnalysisEntity analysisEntity) {
        if(this.analysis != null) {
            return;
        }
        this.analysis = analysisEntity; // 양방향 연관관계 메서드를 맺지 않아도 되는 이유 -> 생성자에서 이미 questions 객체를 넣어줌. 이미 완료됨.
    }

    public void connectAnswer(AnswerEntity answerEntity) {
        this.answer = answerEntity;
    }


    public boolean hasAnswer() {
        return answer != null;
    }

    public Question toModel() {
        return Question.builder()
                .id(id)
                .questionContent(questionContent)
                .analysis(analysis.toModel())
                .answer(answer.toModel())
                .build();
    }
}