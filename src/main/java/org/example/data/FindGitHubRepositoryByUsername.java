package org.example.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.FindRepositoryByUsername;
import org.example.domain.exceptions.UserNotFoundException;
import org.example.domain.model.Branch;
import org.example.domain.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FindGitHubRepositoryByUsername implements FindRepositoryByUsername {

    private static final Logger logger = LoggerFactory.getLogger(FindGitHubRepositoryByUsername.class);

    private final RestTemplate restTemplate;
    private final String githubApiUrl;
    private final ObjectMapper objectMapper;

    public FindGitHubRepositoryByUsername(
            RestTemplate restTemplate,
            @Value("${github.url}") String githubApiUrl
    ) {
        this.restTemplate = restTemplate;
        this.githubApiUrl = githubApiUrl;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Repository> execute(String username) {
        return getRepositories(username);
    }

    public List<Repository> getRepositories(String username) {
        logger.info("Fetching repositories for user: {}", username);
        URI uri = UriComponentsBuilder.fromHttpUrl(githubApiUrl)
                .pathSegment("users", username, "repos")
                .build()
                .toUri();
        try {
            String response = restTemplate.getForObject(uri, String.class);
            return extractRelevantInfo(response, username);
        } catch (HttpClientErrorException e) {
            logger.error("User not found: {}", username, e);
            throw new UserNotFoundException(username);
        }
    }

    public List<Branch> getBranches(String username, String repositoryName) {
        logger.info("Fetching branches for repository: {}", repositoryName);
        URI uri = UriComponentsBuilder.fromHttpUrl(githubApiUrl)
                .pathSegment("repos", username, repositoryName, "branches")
                .build()
                .toUri();

        String response = restTemplate.getForObject(uri, String.class);
        return extractBranches(response);
    }

    private List<Repository> extractRelevantInfo(String jsonResponse, String username) {
        try {
            List<GitHubRepositoryResponse> gitHubRepositoryResponses = Arrays.asList(
                    objectMapper.readValue(
                            jsonResponse,
                            GitHubRepositoryResponse[].class
                    )
            );

            List<Repository> repositories = gitHubRepositoryResponses.stream()
                    .filter(repo -> !repo.fork)
                    .map(repo ->
                            new Repository(
                                    repo.name,
                                    getBranches(username, repo.name)
                            )
                    )
                    .collect(Collectors.toList());
            logger.info("Extracted {} repositories", repositories.size());
            return repositories;

        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response for repositories", e);
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }

    private List<Branch> extractBranches(String jsonResponse) {
        try {

            List<GitHubBranchResponse> gitHubBranchResponses = Arrays.asList(
                    objectMapper.readValue(
                            jsonResponse,
                            GitHubBranchResponse[].class
                    )
            );

            List<Branch> branches = gitHubBranchResponses.stream()
                    .map(gitHubBranchResponse -> new Branch(
                            gitHubBranchResponse.name,
                            gitHubBranchResponse.commit.sha)
                    )
                    .collect(Collectors.toList());
            logger.info("Extracted {} branches", branches.size());
            return branches;

        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response for branches", e);
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }
}
