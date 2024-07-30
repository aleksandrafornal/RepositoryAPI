package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.controller.response.RepositoriesResponse;
import org.example.data.FindGitHubRepositoryByUsername;
import org.example.domain.FindRepositoryByUsername;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {
    private final FindRepositoryByUsername findRepositoryByUsername;

    public RepositoryController(FindRepositoryByUsername findRepositoryByUsername) {
        this.findRepositoryByUsername = findRepositoryByUsername;
    }

    @Operation(summary = "Get repositories, which are not forks from given user")
    @GetMapping("/{username}/repositories")
    public RepositoriesResponse getRepositories(
            @PathVariable String username) {

        var repositories = findRepositoryByUsername.execute(username);

        return new RepositoriesResponse(
                username,
                repositories
        );
    }
}
