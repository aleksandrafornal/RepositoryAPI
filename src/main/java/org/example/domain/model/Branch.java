package org.example.domain.model;

public record Branch(
        String name,
        String lastCommitSHA
) {
}
