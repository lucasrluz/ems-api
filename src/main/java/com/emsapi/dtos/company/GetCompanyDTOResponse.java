package com.emsapi.dtos.company;

public class GetCompanyDTOResponse {
    private String companyId;
    private String name;
    private String description;

    public GetCompanyDTOResponse(String companyId, String name, String description) {
        this.companyId = companyId;
        this.name = name;
        this.description = description;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
