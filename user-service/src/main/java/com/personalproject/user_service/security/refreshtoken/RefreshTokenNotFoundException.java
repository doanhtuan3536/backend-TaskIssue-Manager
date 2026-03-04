package com.personalproject.user_service.security.refreshtoken;

public class RefreshTokenNotFoundException extends Exception {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

}
