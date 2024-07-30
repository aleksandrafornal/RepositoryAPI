package org.example.domain;

import org.example.domain.model.Repository;

import java.util.List;

public interface FindRepositoryByUsername {
    List<Repository> execute(String username);
}
