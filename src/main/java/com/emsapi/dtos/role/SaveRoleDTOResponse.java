package com.emsapi.dtos.role;

public class SaveRoleDTOResponse {
    private String roleId;

    public SaveRoleDTOResponse(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
