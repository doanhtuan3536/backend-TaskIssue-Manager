package com.personalproject.api_gateway.service;

public class OpaqueTokenExpiredException extends Exception {
    public OpaqueTokenExpiredException(String message) {
        super(message);
    }
}
