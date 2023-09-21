package com.emsapi.dtos.company;

public class UpdateCompanyDTORequest {
    private String name;
    private String description;
    
    public UpdateCompanyDTORequest(String name, String description) {
        this.name = name;
        this.description = description;
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
