package com.emsapi.dtos.company;

public class SaveCompanyDTOResponse {
    private String companyId;

    public SaveCompanyDTOResponse(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
