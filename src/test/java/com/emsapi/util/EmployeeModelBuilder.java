package com.emsapi.util;

import java.util.UUID;

import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;

public abstract class EmployeeModelBuilder {
    public static EmployeeModel createWithEmployeeId(RoleModel roleModel, CompanyModel companyModel) {
        return new EmployeeModel(
            UUID.randomUUID(),
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com",
            roleModel,
            companyModel
        );
    }
}
