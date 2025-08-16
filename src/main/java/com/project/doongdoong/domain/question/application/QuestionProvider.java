package com.project.doongdoong.domain.question.application;

import com.project.doongdoong.domain.question.application.port.in.QuestionProvidable;
import com.project.doongdoong.domain.question.domain.Question;
import com.project.doongdoong.domain.question.domain.QuestionContent;
import com.project.doongdoong.domain.question.domain.Questions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionProvider implements QuestionProvidable {

    private static final int FIXED_QUESTION_SIZE = 2;
    private static final int UNFIXED_QUESTION_SIZE = 2;


    @Override
    public List<Question> createRandomQuestions() {
        Questions fixedQuestions = getQuestions(QuestionContent.getFixedQuestionContents(), FIXED_QUESTION_SIZE);
        Questions unFixedQuestions = getQuestions(QuestionContent.getUnFixedQuestionContents(), UNFIXED_QUESTION_SIZE);

        return fixedQuestions.addQuestions(unFixedQuestions);
    }

    private Questions getQuestions(List<QuestionContent> questionContents, int size) {
        List<Question> questionList = getQuestionListFrom(questionContents);

        Questions questionsCandidate = Questions.from(questionList);

        return questionsCandidate.extractRandomQuestions(size);
    }

    private List<Question> getQuestionListFrom(List<QuestionContent> questionContents) {
        return questionContents.stream()
                .map(Question::of)
                .collect(Collectors.toList());
    }


}