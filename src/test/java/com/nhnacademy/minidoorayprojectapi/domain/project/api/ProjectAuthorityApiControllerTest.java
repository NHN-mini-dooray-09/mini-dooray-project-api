package com.nhnacademy.minidoorayprojectapi.domain.project.api;

import com.nhnacademy.minidoorayprojectapi.domain.project.dto.response.ProjectAuthorityDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.service.ProjectAuthorityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectAuthorityApiController.class)
class ProjectAuthorityApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectAuthorityService projectAuthorityService;

    @Test
    void getProjectAuthority() throws Exception{
        ProjectAuthorityDto projectAuthorityDto = new ProjectAuthorityDto(2L,"PROJECT_MEMBER");

        when(projectAuthorityService.getProjectAuthority(any(),any()))
                .thenReturn(projectAuthorityDto);

        mockMvc.perform(get("/projects/1/authorize")
                .param("member-seq","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberSeq",equalTo(projectAuthorityDto.getMemberSeq().intValue())))
                .andExpect(jsonPath("$.projectAuthority",equalTo(projectAuthorityDto.getProjectAuthority())));

        verify(projectAuthorityService, times(1))
                .getProjectAuthority(any(),any());
    }
}