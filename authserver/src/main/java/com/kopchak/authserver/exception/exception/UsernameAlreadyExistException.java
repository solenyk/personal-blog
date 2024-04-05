package com.kopchak.authserver.exception.exception;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException(String username) {
        super(String.format("The user with the username: %s already exist!", username));
    }
}