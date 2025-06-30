package com.project.doongdoong.domain.analysis.domain;

import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.domain.QuestionEntity;
import com.project.doongdoong.domain.user.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Analysis {
    private Long id;

    private double feelingState;

    private LocalDate analyzeTime;

    private UserEntity user;

    private List<QuestionEntity> questions = new ArrayList<>();

    private List<Answer> answers = new ArrayList<>();

    private static final int MAX_ANSWER_COUNT = 4;

    public static AnalysisEntity of(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        return AnalysisEntity.builder()
                .userEntity(userEntity)
                .questionEntities(questionEntities)
                .build();
    }

    @Builder
    public Analysis(UserEntity userEntity, List<QuestionEntity> questionEntities) {
        this.feelingState = 0;
        this.questions = questionEntities;
        this.user = userEntity;
    }

    public boolean hasAllAnswer() {
        return this.answers.size() == MAX_ANSWER_COUNT;
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

  /*  public void addUser(User user) {
        this.user = user;
        user.getAnalysisList().add(this);
    }*/

}
