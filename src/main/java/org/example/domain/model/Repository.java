package org.example.domain.model;

import java.util.List;

public record Repository(
        String name,
        List<Branch> branches
) {
}
