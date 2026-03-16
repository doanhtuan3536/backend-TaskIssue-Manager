package com.personalproject.api_gateway.serviceClient;

public class JwtValidationException extends Exception{
    public JwtValidationException(String message) {
        super(message);
    }
}
