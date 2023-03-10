package com.sivalabs.tcdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GithubController {

	private final GithubService githubService;

	@GetMapping("/users/{username}")
	public GitHubUser getGithubUserProfile(@PathVariable String username) {
		return githubService.getGithubUserProfile(username);
	}

}