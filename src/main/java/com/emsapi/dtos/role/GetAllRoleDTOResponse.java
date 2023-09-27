package com.emsapi.dtos.role;

public class GetAllRoleDTOResponse {
    private String roleId;
    private String name;

    public GetAllRoleDTOResponse(String roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
