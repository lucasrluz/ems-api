package com.emsapi.util;

import com.emsapi.dtos.UpdateUserDTORequest;

public abstract class UpdateUserDTORequestBuilder {
    public static UpdateUserDTORequest createWithValidData() {
        return new UpdateUserDTORequest("bar", "foo");
    }

    public static UpdateUserDTORequest createWithInvalidFirstName() {
        return new UpdateUserDTORequest("", "foo");
    }

    public static UpdateUserDTORequest createWithInvalidLastName() {
        return new UpdateUserDTORequest("bar", "");
    }
}
