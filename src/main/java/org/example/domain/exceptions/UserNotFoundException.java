package org.example.domain.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String username) {
        super("User " + username + " not found");
    }
}
