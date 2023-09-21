package com.emsapi.dtos.company;

public class UpdateCompanyDTOResponse {
    private String companyId;

    public UpdateCompanyDTOResponse(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
