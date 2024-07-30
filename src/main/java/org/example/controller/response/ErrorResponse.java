package org.example.controller.response;

public record ErrorResponse (
        int status,
        String message
) {}
