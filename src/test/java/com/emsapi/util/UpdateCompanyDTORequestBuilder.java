package com.emsapi.util;

import com.emsapi.dtos.company.UpdateCompanyDTORequest;

public abstract class UpdateCompanyDTORequestBuilder {
    public static UpdateCompanyDTORequest createWithValidData() {
        return new UpdateCompanyDTORequest("bar", "foo", "foo@gmail.com", "123");
    }
}
