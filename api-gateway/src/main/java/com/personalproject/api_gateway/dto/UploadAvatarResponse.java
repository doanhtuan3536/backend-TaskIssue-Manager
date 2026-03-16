package com.personalproject.api_gateway.dto;

public class UploadAvatarResponse {

    private String url;

    public UploadAvatarResponse(String url) {
        this.url = url;
    }

    public UploadAvatarResponse() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
