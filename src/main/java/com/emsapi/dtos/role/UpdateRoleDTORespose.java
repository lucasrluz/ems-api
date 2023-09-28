package com.emsapi.dtos.role;

public class UpdateRoleDTORespose {
    private String roleId;

    public UpdateRoleDTORespose(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
