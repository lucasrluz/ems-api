package com.emsapi.dtos.authentication;

public class SignUpDTOResponse {
    private String userId;

    public SignUpDTOResponse() {}

    public SignUpDTOResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
