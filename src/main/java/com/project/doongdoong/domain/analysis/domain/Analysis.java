package com.project.doongdoong.domain.analysis.domain;

import com.project.doongdoong.domain.answer.domain.Answer;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * [Analysis 도메인 객체지행 의견]
 * 과거 Analysis 에서 답변의 개수를 지정했다. 예를 들어, 서비스에서 질문과 답변은 하나의 분석의 총 4개가 필요하다.
 * 하지만 Analysis 질문이 몇개인지 알아서는 안 된다.
 * private List<Question> questions = new ArrayList<>(); 와 같이 사용하면 질문의 수를 파악할 수 없다. 질문 리스트를 사용하는 Analysis
 * 입장에서 몇 개의 질문을 가져야하는지 남기는 것은 잘못된 책임이다. 오히려 Question 일급 컬렉션을 통해 질문의 개수를 가지고 있는 것이 책임에 대해 올바르다.
 * 따라서 변경한 hasAllAnswer 메소드도 질문과 답변의 개수가 일치하는지 보면 된다.
 * 이 부분은 아키텍처 리팩토링과 관련 없어 적용하지 않으나 추후 숙제 혹은 객체지향 시야를 기르기 위한 주석으로 남긴다.
 */
@Getter
public class Analysis {
    private Long id;

    private double feelingState;

    private LocalDate analyzedDate;

    private User user;

    private List<Question> questions = new ArrayList<>();

    private List<Answer> answers = new ArrayList<>();

    public static Analysis of(User user, List<Question> questions) {

        return Analysis.builder()
                .user(user)
                .questions(questions)
                .build();
    }

    @Builder
    public Analysis(User user, List<Question> questions) {
        this.feelingState = 0;
        this.questions = questions;
        this.user = user;
    }

    public boolean hasAllAnswer() {
        return questions.size() == answers.size();
    }


    public void changeFeelingStateAndAnalyzeTime(double feelingState, LocalDate analyzeTime) {
        this.feelingState = feelingState;
        this.analyzedDate = analyzeTime;
    }

    public boolean equalsAnalyzeTimeTo(LocalDate time) {
        if (this.analyzedDate == null || time == null) {
            return false;
        }
        return time.equals(this.analyzedDate);
    }

    public boolean isAlreadyAnalyzed() {
        return this.analyzedDate != null;
    }

}
