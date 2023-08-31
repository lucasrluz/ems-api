package com.emsapi.util;

import com.emsapi.dtos.SignUpDTORequest;

public abstract class SignUpDTORequestBuilder {
    public static SignUpDTORequest createWithValidData() {
        return new SignUpDTORequest("foo", "bar", "foobar@gmail.com", "123");
    }
}
