package com.emsapi.util;

import com.emsapi.dtos.employee.UpdateEmployeeDTORequest;

public abstract class UpdateEmployeeDTORequestBuilder {
    public static UpdateEmployeeDTORequest createWithValidData(String roleId, String companyId) {
        return new UpdateEmployeeDTORequest("bar", "foo", "22", "9809 Margo Street", companyId, "barfoo@gmail.com", roleId);
    }
}
