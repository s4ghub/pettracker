package com.example.pettracker.pettracker.exceptionhandling;

import lombok.NonNull;

public class BaduserInputException extends RuntimeException{
    public BaduserInputException(@NonNull String message) {
        super(message);
    }
}
