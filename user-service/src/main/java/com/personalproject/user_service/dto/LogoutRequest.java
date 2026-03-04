package com.personalproject.user_service.dto;

public class LogoutRequest {
    private Integer refreshTokenId;

    public Integer getRefreshTokenId() {
        return refreshTokenId;
    }

    public void setRefreshTokenId(Integer refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }

    @Override
    public String toString() {
        return "LogoutRequest{" +
                "refreshTokenId=" + refreshTokenId +
                '}';
    }

    public LogoutRequest(Integer refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }
}
