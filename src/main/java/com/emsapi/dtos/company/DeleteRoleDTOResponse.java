package com.emsapi.dtos.company;

public class DeleteRoleDTOResponse {
    private String roleId;

    public DeleteRoleDTOResponse(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
