package com.emsapi.dtos.authentication;

public class SignUpDTOResponse {
    private String companyId;

    public SignUpDTOResponse() {}

    public SignUpDTOResponse(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
