package com.emsapi.util;

import java.util.ArrayList;
import java.util.List;
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

    public static EmployeeModel createWithEmptyEmployeeId(RoleModel roleModel, CompanyModel companyModel) {
        return new EmployeeModel(
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com",
            roleModel,
            companyModel
        );
    }

    public static List<EmployeeModel> createListWithEmployeeId(RoleModel roleModel, CompanyModel companyModel) {
        List<EmployeeModel> employeeModels = new ArrayList<EmployeeModel>();

        employeeModels.add(new EmployeeModel(
            UUID.randomUUID(),
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com",
            roleModel,
            companyModel
        ));

        employeeModels.add(new EmployeeModel(
            UUID.randomUUID(),
            "bar",
            "foo",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "barfoo@gmail.com",
            roleModel,
            companyModel
        ));

        employeeModels.add(new EmployeeModel(
            UUID.randomUUID(),
            "foo",
            "baz",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobaz@gmail.com",
            roleModel,
            companyModel
        ));

        return employeeModels;
    }

    public static List<EmployeeModel> createListWithEmptyEmployeeId(RoleModel roleModel, CompanyModel companyModel) {
        List<EmployeeModel> employeeModels = new ArrayList<EmployeeModel>();

        employeeModels.add(new EmployeeModel(
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com",
            roleModel,
            companyModel
        ));

        employeeModels.add(new EmployeeModel(
            "bar",
            "foo",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "barfoo@gmail.com",
            roleModel,
            companyModel
        ));

        employeeModels.add(new EmployeeModel(
            "foo",
            "baz",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobaz@gmail.com",
            roleModel,
            companyModel
        ));

        return employeeModels;
    }
}
