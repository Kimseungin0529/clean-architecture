package com.project.doongdoong.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doongdoong.domain.analysis.adapter.in.web.AnalysisController;
import com.project.doongdoong.domain.analysis.application.port.in.AnalysisService;
import com.project.doongdoong.domain.answer.application.port.in.AnswerService;
import com.project.doongdoong.domain.counsel.adapter.in.web.CounselController;
import com.project.doongdoong.domain.counsel.application.port.in.CounselService;
import com.project.doongdoong.domain.counsel.application.port.in.CounselStatisticsService;
import com.project.doongdoong.domain.user.controller.UserController;
import com.project.doongdoong.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

    @WebMvcTest(controllers = {CounselController.class, AnalysisController.class, UserController.class} /* 추가로 필요한 컨트롤러 클래스 지정 */)
    public abstract class ControllerTestSupport {
        @Autowired
        protected MockMvc mockMvc;
        @MockBean
        protected CounselService counselService;
        @MockBean
        CounselStatisticsService counselStatisticsService;
        @MockBean
        protected AnalysisService analysisService;
        @MockBean
        protected AnswerService answerService;
        @MockBean
        protected UserService userService;
        @Autowired
        protected ObjectMapper objectMapper;
    }
