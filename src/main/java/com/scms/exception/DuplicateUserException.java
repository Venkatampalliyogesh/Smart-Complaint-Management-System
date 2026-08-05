package com.scms.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException() {
        super();
    }

    public DuplicateUserException(String message) {
        super(message);
    }

    public DuplicateUserException(String email) {
        super("User already exists with email: " + email);
    }

}