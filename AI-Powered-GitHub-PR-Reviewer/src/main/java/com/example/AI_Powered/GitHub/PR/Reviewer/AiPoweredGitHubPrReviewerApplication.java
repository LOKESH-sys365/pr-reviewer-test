package com.example.AI_Powered.GitHub.PR.Reviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AiPoweredGitHubPrReviewerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiPoweredGitHubPrReviewerApplication.class, args);
	}

}
