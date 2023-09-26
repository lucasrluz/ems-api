package com.emsapi.util;

import com.emsapi.dtos.role.SaveRoleDTORequest;

public abstract class SaveRoleDTORequestBuilder {
    public static SaveRoleDTORequest createWithValidData() {
        return new SaveRoleDTORequest("foo");
    }

    public static SaveRoleDTORequest createWithEmptyName() {
        return new SaveRoleDTORequest("");
    }
}
