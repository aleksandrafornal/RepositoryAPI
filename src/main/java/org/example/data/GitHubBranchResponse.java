package org.example.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubBranchResponse {
    public String name;
    public Commit commit;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Commit {
        public String sha;
    }
}
