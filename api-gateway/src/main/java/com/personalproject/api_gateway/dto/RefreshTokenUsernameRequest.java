package com.personalproject.api_gateway.dto;

public class RefreshTokenUsernameRequest {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RefreshTokenUsernameRequest{" +
                "username='" + username + '\'' +
                '}';
    }

    public RefreshTokenUsernameRequest(String username) {
        this.username = username;
    }

    public RefreshTokenUsernameRequest() {
    }
}
