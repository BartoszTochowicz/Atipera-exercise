package com.atipera.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atipera.model.BranchInfo;
import com.atipera.service.GitHubService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/github")
@Tag(name ="GitHub API", description = "Operations on GitHub API")
public class GitHubController{
    private final GitHubService githubService;
    public GitHubController(GitHubService githubService){
        this.githubService = githubService;
    }
    @Operation(summary = "User's repositories list")
    @GetMapping("/repos")
    public ResponseEntity<List<BranchInfo>> getRepositories(@RequestParam String username){
        List<BranchInfo> repos = githubService.fetchRepos(username);
        return ResponseEntity.ok(repos);
    }
}