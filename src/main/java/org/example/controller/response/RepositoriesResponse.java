package org.example.controller.response;

import org.example.domain.model.Repository;

import java.util.List;

public record RepositoriesResponse(
        String ownerLogin,
        List<Repository> repositories
) {
}
