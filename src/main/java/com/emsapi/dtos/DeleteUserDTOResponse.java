package com.emsapi.dtos;

public class DeleteUserDTOResponse {
    private String userId;

    public DeleteUserDTOResponse(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
