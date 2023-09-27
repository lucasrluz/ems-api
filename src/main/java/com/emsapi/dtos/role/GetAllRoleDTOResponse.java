package com.emsapi.dtos.role;

public class GetAllRoleDTOResponse {
    private String name;

    public GetAllRoleDTOResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
