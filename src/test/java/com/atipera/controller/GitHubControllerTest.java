package com.atipera.controller;

import com.atipera.model.BranchInfo;
import com.atipera.service.GitHubService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GitHubController.class)
public class GitHubControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GitHubService githubService;

    @Test
    public void shouldGetRepos() throws Exception{
        //given
        BranchInfo testBranchInfo = new BranchInfo();
        testBranchInfo.setOwnerLogin("btocho");
        testBranchInfo.setRepoName("test");
        testBranchInfo.setBranchName("main");
        testBranchInfo.setLastCommitSha("skibidi");

        when(githubService.fetchRepos("btocho"))
            .thenReturn(List.of(testBranchInfo));
        //when

        var result = mockMvc.perform(get("/api/github/repos").param("username","btocho"))
            .andDo(print())
            .andExpect(status().is(200))
            .andExpect(jsonPath("$[0].ownerLogin").value("btocho"))
            .andExpect(jsonPath("$[0].repoName").value("test"))
            .andExpect(jsonPath("$[0].branchName").value("main"))
            .andReturn();
        //then
        List<BranchInfo> list = objectMapper.readValue(result.getResponse().getContentAsString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class,
                     BranchInfo.class));
        assertThat(list).hasSize(1)
            .first()
            .usingRecursiveComparison()
            .isEqualTo(testBranchInfo);

    }
}
