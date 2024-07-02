package com.example.userservice.controller;

public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    // Getter
    public String getError() {
        return error;
    }
}
