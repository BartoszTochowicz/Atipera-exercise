package com.atipera.service;

import com.atipera.model.BranchInfo;
import com.atipera.exceptions.UsernameException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class GitHubService {
    private final String token;
    public GitHubService(){
        this.token = Dotenv.load().get("GITHUB_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("GITHUB_TOKEN not set in .env");
        }
    }
    public List<BranchInfo> fetchRepos(String username){
        List<BranchInfo> result = new ArrayList<>();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.github.com/users/" + username + "/repos"))
                .header("Authorization", "token " + token)
                .header("Accept", "application/vnd.github.v3+json")
                .GET()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if(response.statusCode()==404){
                throw new UsernameException("GitHub username "+username+" not found");
            }
            if(response.statusCode() != 200){
                throw new ResponseStatusException(HttpStatus.valueOf(response.statusCode()), "GitHub error");
            }
            JSONArray repos = new JSONArray(response.body());

            for (Object obj : repos){
                JSONObject repo = (JSONObject) obj;
                if(repo.getBoolean("fork")){
                    continue;
                }
                String repoName = repo.getString("name");
                String ownerLogin = repo.getJSONObject("owner").getString("login");

                HttpRequest branchRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.github.com/repos/"+username+"/"+repoName+"/branches"))
                    .header("Authorization", "token " + token)
                    .header("Accept", "application/vnd.github.v3+json")
                    .GET()
                    .build();
                    
                HttpResponse<String> branchResponse =client.send(branchRequest,HttpResponse.BodyHandlers.ofString()); 
                
                JSONArray branches = new JSONArray(branchResponse.body());

                for (Object b : branches){
                    JSONObject branch = (JSONObject) b;
                    String branchName =  branch.getString("name");
                    String sha = branch.getJSONObject("commit").getString("sha");

                    BranchInfo branchInfo = new BranchInfo();
                    branchInfo.setRepoName(repoName);
                    branchInfo.setOwnerLogin(ownerLogin);
                    branchInfo.setBranchName(branchName);
                    branchInfo.setLastCommitSha(sha);
                    
                    result.add(branchInfo);
                }
            }
        } catch (UsernameException e) {
            throw e;
        } catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Internal error",e);
        }
        return result;
    }
}
