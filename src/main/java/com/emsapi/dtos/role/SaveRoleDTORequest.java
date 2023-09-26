package com.emsapi.dtos.role;

public class SaveRoleDTORequest {
    private String name;

    public SaveRoleDTORequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSame(String name) {
        this.name = name;
    }
}
