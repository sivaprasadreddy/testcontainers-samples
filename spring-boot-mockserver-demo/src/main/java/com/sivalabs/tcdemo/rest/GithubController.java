package com.sivalabs.tcdemo.rest;

import com.sivalabs.tcdemo.domain.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/users/{username}")
    public String getGithubUserProfile(@PathVariable String username) {
        return githubService.getGithubUserProfile(username);
    }
}