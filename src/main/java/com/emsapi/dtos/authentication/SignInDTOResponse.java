package com.emsapi.dtos.authentication;

public class SignInDTOResponse {
    private String jwt;

    public SignInDTOResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
