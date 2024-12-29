package com.project.doongdoong.domain.counsel.controller;

import com.project.doongdoong.domain.counsel.dto.request.CounselCreateRequest;
import com.project.doongdoong.domain.counsel.dto.response.CounselResultResponse;
import com.project.doongdoong.module.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CounselControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("상담에 대한 결과가 나온다.")
    @WithMockUser(username = "123456_APPLE")
    void createCounsel() throws Exception {
        // given
        CounselCreateRequest request = CounselCreateRequest.builder()
                .analysisId(null)
                .question("요즘 가족이 맨날 싸워. 청소 관련 성향의 차이로 다투기 시작했어. 사소한 이유가 너무 커져서 폭언을 하기도 해. 어떻게 해결해야 할까?")
                .counselType("가족")
                .build();

        CounselResultResponse response = new CounselResultResponse(
                1L, "counselContent 입니다.", "imageUrl 입니다."
        );

        when(counselService.consult(anyString(), any(CounselCreateRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/counsel")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/counsel/1"))
                .andExpect(jsonPath("$.data.counselId").value(1L))
                .andExpect(jsonPath("$.data.counselContent").value("counselContent 입니다."))
                .andExpect(jsonPath("$.data.imageUrl").value("imageUrl 입니다."));

    }

    @Test
    @DisplayName("상담을 하기 위해서는 상담 카테코리와 상담 질문은 필수입니다.")
    @WithMockUser
    void createCounselWithEmptyCounselType() throws Exception {
        //given
        CounselCreateRequest request = CounselCreateRequest.builder()
                .analysisId(null)
                .counselType(null)
                .question(null)
                .build();

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/counsel")
                                .with(csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(0))
                .andExpect(jsonPath("$.message").value("잘못된 입력 형식이 존재합니다."))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", containsInAnyOrder(
                                "카테코리는 필수입니다.", "상담 질문은 필수입니다.")
                        )
                );


    }

}