package com.emsapi.util;

import com.emsapi.dtos.company.SaveCompanyDTORequest;

public abstract class SaveCompanyDTORequestBuilder {
    public static SaveCompanyDTORequest createWithValidData() {
        return new SaveCompanyDTORequest("foo", "bar");
    }

    public static SaveCompanyDTORequest createWithEmptyName() {
        return new SaveCompanyDTORequest("", "bar");
    }

    public static SaveCompanyDTORequest createWithEmptyDescription() {
        return new SaveCompanyDTORequest("foo", "");
    }
}
