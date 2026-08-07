package com.example.AI_Powered.GitHub.PR.Reviewer;



import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestFileDetail;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final GitHub gitHub;

    // Constructor injection — cleaner than instance initializer
    public GitHubService(@Value("${github.token}") String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("github.token is not set in application.properties");
        }
        try {
            this.gitHub = new GitHubBuilder()
                    .withOAuthToken(token)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to GitHub API", e);
        }
    }

    public List<Filldiff> getPullRequestFiles(String repoFullName, int prNumber) throws IOException {
        GHRepository repo = gitHub.getRepository(repoFullName);
        GHPullRequest pr = repo.getPullRequest(prNumber);

        // Convert GitHub's internal objects to YOUR clean model
        return pr.listFiles().toList().stream()
                .map(this::convertToFileDiff)
                .collect(Collectors.toList());
    }

    private Filldiff convertToFileDiff(GHPullRequestFileDetail fileDetail) {
        Filldiff diff = new Filldiff();
        diff.setFilename(fileDetail.getFilename());

        diff.setPatch(fileDetail.getPatch());        // This is the raw diff text
        diff.setStatus(fileDetail.getStatus());
        diff.setDeletion(fileDetail.getDeletions());
        diff.setAddition(fileDetail.getAdditions());

        return diff;
    }
}