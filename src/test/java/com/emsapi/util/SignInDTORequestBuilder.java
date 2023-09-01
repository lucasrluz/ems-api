package com.emsapi.util;

import com.emsapi.dtos.SignInDTORequest;

public abstract class SignInDTORequestBuilder {
    public static SignInDTORequest createWithValidData() {
        return new SignInDTORequest("foobar@gmail.com", "123");
    }
}
