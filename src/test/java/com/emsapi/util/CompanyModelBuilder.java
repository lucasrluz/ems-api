package com.emsapi.util;

import java.util.UUID;

import com.emsapi.models.CompanyModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class CompanyModelBuilder {
    public static CompanyModel createWithCompanyId() {
        return new CompanyModel(UUID.randomUUID(), "foo", "bar", "foo@gmail.com", BCrypt.withDefaults().hashToString(12, "123".toCharArray()));
    }

    public static CompanyModel createWithEmptyCompanyId() {
        return new CompanyModel("foo", "bar", "foo@gmail.com", BCrypt.withDefaults().hashToString(12, "123".toCharArray()));
    }
}
