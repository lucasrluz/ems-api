package com.emsapi.util;

import com.emsapi.dtos.employee.SaveEmployeeDTORequest;

public abstract class SaveEmployeeDTORequestBuilder {
    public static SaveEmployeeDTORequest createWithValidData(String companyId, String roleId) {
        return new SaveEmployeeDTORequest(
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com",
            companyId,
            roleId
        ); 
    }
}
