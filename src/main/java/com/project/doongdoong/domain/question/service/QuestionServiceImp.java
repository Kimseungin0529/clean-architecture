package com.project.doongdoong.domain.question.service;

import com.project.doongdoong.domain.question.model.Question;
import com.project.doongdoong.domain.question.model.QuestionContent;
import com.project.doongdoong.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionServiceImp implements QuestionService {

    private final QuestionRepository questionRepository;
    private static final int FIXED_QUESTION_SIZE = 2;
    private static final int UNFIXED_QUESTION_SIZE = 2;



    @Override
    public Question createFixedQuestion() {
        QuestionContent questionContent = QuestionContent.provideFixedQuestionContent();

        Question question = Question.builder()
                .questionContent(questionContent)
                .build();
        //questionRepository.save(question);

        return question;

    }

    @Override
    public Question createUnFixedQuestion() {
        QuestionContent questionContent = QuestionContent.provideUnFixedQuestionContent();

        Question question = Question.builder()
                .questionContent(questionContent)
                .build();
        //questionRepository.save(question);

        return question;

    }

    @Transactional
    @Override
    public List<Question> createQuestions() {
        Set<Question> fixedQuestionList = getFixedQuestionList(FIXED_QUESTION_SIZE);
        Set<Question> unFixedQuestionList = getUnFixedQuestionList(UNFIXED_QUESTION_SIZE);

        List<Question> mergedQuestionList = new ArrayList<>(fixedQuestionList);
        mergedQuestionList.addAll(unFixedQuestionList);

        questionRepository.saveAll(mergedQuestionList);

        return mergedQuestionList;

    }

    private Set<Question> getFixedQuestionList(int size) {
        Set<Question> questions = new HashSet<>();
        while(questions.size() < FIXED_QUESTION_SIZE){
            Question fixedQuestion = createFixedQuestion();
            questions.add(fixedQuestion);
        }
        return questions;
    }

    private Set<Question> getUnFixedQuestionList(int size) {
        Set<Question> questions = new HashSet<>();
        while(questions.size() < UNFIXED_QUESTION_SIZE){
            Question unFixedQuestion = createUnFixedQuestion();
            questions.add(unFixedQuestion);
        }
        return questions;
    }

}
