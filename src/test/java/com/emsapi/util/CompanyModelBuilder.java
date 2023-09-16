package com.emsapi.util;

import java.util.UUID;

import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;

public abstract class CompanyModelBuilder {
    public static CompanyModel createWithCompanyId(UserModel userModel) {
        return new CompanyModel(UUID.randomUUID(), "foo", "bar", userModel);
    }
}
