package com.project.doongdoong.domain.counsel.service;

import com.project.doongdoong.domain.analysis.exception.AnalysisAccessDeny;
import com.project.doongdoong.domain.analysis.model.Analysis;
import com.project.doongdoong.domain.analysis.repository.AnalysisRepository;
import com.project.doongdoong.domain.counsel.dto.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.CounselResultResponse;
import com.project.doongdoong.domain.counsel.model.Counsel;
import com.project.doongdoong.domain.counsel.repository.CounselRepository;
import com.project.doongdoong.domain.user.exeception.UserNotFoundException;
import com.project.doongdoong.domain.user.model.SocialType;
import com.project.doongdoong.domain.user.model.User;
import com.project.doongdoong.domain.user.repository.UserRepository;
import com.project.doongdoong.global.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CounselServiceImpl implements CounselService {

    private final AnalysisRepository analysisRepository;
    private final CounselRepository counselRepository;
    private final UserRepository userRepository;
    private final WebClientUtil webClientUtil;


    @Transactional
    @Override
    public CounselResultResponse consult(String socialId, CounselCreateRequest request) {
        String[] values = parseUniqueValue(socialId); // 사용자 정보 찾기
        User user = userRepository.findBySocialTypeAndSocialId(SocialType.customValueOf(values[1]), values[0])
                .orElseThrow(() -> new UserNotFoundException());

        Counsel counsel = Counsel.builder() // 상담 객체 생성
                .question(request.getQuestion())
                .counselType(request.getCounselType())
                .build();

        if(request.getAnalysisId() != null){ // 기존 분석 결과 반영하기
            Analysis findAnalysis = analysisRepository.findByUserAndId
                    (user, request.getAnalysisId()).orElseThrow(() -> new AnalysisAccessDeny());
            counsel.addAnalysis(findAnalysis); // 연관관계 매핑
        }

        HashMap<String, String> parameters = new HashMap<String, String>( ); // 외부 API request 설정
        parameters.put("question", counsel.getQuestion());
        Optional.ofNullable(counsel.getAnalysis())
                .ifPresent(analysis -> {
                    for(int i=0; i<analysis.getAnswers().size(); i++){
                        parameters.put("analysisQuestion" + i, analysis.getAnswers().get(i).getContent());
                    }
                });

        String consultResult = webClientUtil.callConsult(parameters);

        counsel.saveAnswer(consultResult);
        Counsel savedCounsel = counselRepository.save(counsel);

        return  CounselResultResponse.builder()
                .counselId(savedCounsel.getId())
                .counselResult(consultResult)
                .build();

    }

    @Override
    public void findCouselContent() {

    }

    @Override
    public void findConusels() {

    }

    private static String[] parseUniqueValue(String uniqueValue) {
        String[] values = uniqueValue.split("_"); // 사용자 찾기
        return values;
    }
}
