package com.project.doongdoong.domain.counsel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.doongdoong.domain.counsel.model.CounselType;
import com.project.doongdoong.domain.counsel.service.CounselService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CounselController.class) // 컨트롤러 테스트를 위해 관련 빈만 등록해줌
class CounselControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    CounselService counselService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("신규 상담을 생성한다.")
    @WithMockUser()
    void createCounsel() throws Exception {
     /*   //given
        CounselCreateRequest request = CounselCreateRequest.builder()
                .feellingScore(72.5)
                .counselType(CounselType.FAMILY)
                .build();


        //when
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/counsel")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
        //then
*/
    }

    @Test
    @DisplayName("신규 상담을 생성할 때, 상담 카테코리는 필수입니다..")
    @WithMockUser
    void createCounselWithEmptyCounselType() throws Exception {
      /*  //given
        CounselCreateRequest request = CounselCreateRequest.builder()
                .feellingScore(72.5)
                .build();

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/counsel")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(0))
                .andExpect(jsonPath("$.message").value("잘못된 입력 형식이 존재합니다."))
                .andExpect(jsonPath("$.details").value("상담 카테코리는 필수입니다."));
        //then*/

    }

  /*  @Test
    @DisplayName("신규 상담을 생성할 때, 상담 카테코리에 해당하는 값이 없습니다. .")
    @WithMockUser
    void createCounselWithInvalidCounselType() throws Exception {
        //given

        String request = "{\n" +
                "    \"feellingScore\": 72.4,\n" +
                "    \"counselType\": \"HI\"\n" +
                "}";

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/counsel")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(3))
                .andExpect(jsonPath("$.message").value("HI라는 타입은 없습니다."));
        //then

    }
*/

}