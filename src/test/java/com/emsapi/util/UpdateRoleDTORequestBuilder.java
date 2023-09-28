package com.emsapi.util;

import com.emsapi.dtos.role.UpdateRoleDTORequest;

public abstract class UpdateRoleDTORequestBuilder {
    public static UpdateRoleDTORequest createWithValidData() {
        return new UpdateRoleDTORequest("bar");
    }
}
