package com.kopchak.authserver.exception.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String username) {
        super(String.format("User with username: %s is not found!", username));
    }
}
