package com.atipera.model;

public class BranchInfo {
    private String repoName;
    private String ownerLogin;
    private String branchName;
    private String lastCommitSha;
    public String getRepoName(){
        return repoName;
    }
    public void setRepoName(String repoName){
        this.repoName = repoName;
    }
    public String getOwnerLogin(){
        return ownerLogin;
    }
    public void setOwnerLogin(String ownerLogin){
        this.ownerLogin = ownerLogin;
    }
    public String getBranchName(){
        return branchName;
    }
    public void setBranchName(String branchName){
        this.branchName = branchName;
    }
    public String getLastCommitSha(){
        return lastCommitSha;
    }
    public void setLastCommitSha(String lastCommitSha){
        this.lastCommitSha = lastCommitSha;
    }
}
