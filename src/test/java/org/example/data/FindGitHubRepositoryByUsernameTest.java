package org.example.data;

import org.example.domain.model.Branch;
import org.example.domain.model.Repository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FindGitHubRepositoryByUsernameTest {

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private final String githubApiUrl = "https://api.mock.com";

    private final FindGitHubRepositoryByUsername findGitHubRepositoryByUsername =
            new FindGitHubRepositoryByUsername(
                    restTemplate,
                    githubApiUrl
            );

    @Test
    public void shouldReturnRepositories() {
        //Given
        String username = "testUsername";
        var jsonResponse = """
                [
                  {
                      "name": "repo1",
                      "fork": false
                   },
                   {
                      "name": "repo2",
                      "fork": true
                    }
                 ];
                """;
        var branchesJson = """
                [
                    {
                        "name":"main",
                        "commit":{
                            "sha":"123456"
                        }
                     },
                     {
                        "name":"dev",
                        "commit":{
                            "sha":"654321"
                        }
                      }
                 ];
                """;

        when(restTemplate.getForObject(any(), any())).thenAnswer(invocation -> {
            URI uri = invocation.getArgument(0);
            if (uri.getPath().endsWith("/repos")) {
                return jsonResponse;
            } else if (uri.getPath().contains("/branches")) {
                return branchesJson;
            }
            return null;
        });

        //When
        List<Repository> repositories = findGitHubRepositoryByUsername.getRepositories(username);

        //Then
        assertEquals(1, repositories.size());
        assertEquals("repo1", repositories.getFirst().name());
    }

    @Test
    public void shouldReturnBranches() {
        //Given
        String username = "testUsername";
        String repository = "testRepository";
        String branchesJson = """
                [
                    {
                        "name":"main",
                        "commit":{
                            "sha":"123456"
                        }
                     },
                     {
                        "name":"dev",
                        "commit":{
                            "sha":"654321"
                        }
                      }
                 ];
                """;
        when(restTemplate.getForObject(any(), any())).thenReturn(branchesJson);

        //When
        List<Branch> branches = findGitHubRepositoryByUsername.getBranches(username, repository);

        //Then
        assertEquals(2, branches.size());
    }
}