package com.personalproject.api_gateway.models;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class TokenInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accessToken;      // JWT from user service
    private Long userId;
    private String username;
    private String fullName;
    private String role;
    private Instant createdAt;
    private Instant lastAccessTime;
    private String ipAddress;
    private String userAgent;

    public TokenInfo(String accessToken, Long userId, String username, String fullName, String role, Instant createdAt, Instant lastAccessTime, String ipAddress, String userAgent) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
        this.lastAccessTime = lastAccessTime;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public TokenInfo() {}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Instant lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
