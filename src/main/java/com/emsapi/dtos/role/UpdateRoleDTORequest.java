package com.emsapi.dtos.role;

public class UpdateRoleDTORequest {
    private String name;

    public UpdateRoleDTORequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
