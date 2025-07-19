package com.atipera.controller;

import com.atipera.model.BranchInfo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetRepos() throws Exception{
        //given
        String username = "BartoszTochowicz";

        //when
        ResponseEntity<BranchInfo[]> response = restTemplate.getForEntity(
            "/api/github/repos?username="+username,
            BranchInfo[].class
        );
        //then

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();

        for (BranchInfo branchInfo : response.getBody()) {
            assertThat(branchInfo.getOwnerLogin()).isEqualTo(username);
            assertThat(branchInfo.getRepoName()).isNotBlank();
            assertThat(branchInfo.getBranchName()).isNotBlank();
            assertThat(branchInfo.getLastCommitSha()).isNotBlank().hasSize(40);
        }
        List<String> repoNames = Arrays.stream(response.getBody())
            .map(BranchInfo::getRepoName)
            .distinct()
            .collect(Collectors.toList());
        
        assertThat(repoNames).doesNotContain("Test2");
    }
}
