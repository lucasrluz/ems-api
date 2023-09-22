package com.emsapi.util;

import com.emsapi.dtos.authentication.SignUpDTORequest;

public abstract class SignUpDTORequestBuilder {
    public static SignUpDTORequest createWithValidData() {
        return new SignUpDTORequest("foo", "bar", "foobar@gmail.com", "123");
    }

    public static SignUpDTORequest createWithEmptyFirstName() {
        return new SignUpDTORequest("", "bar", "foobar@gmail.com", "123");
    }

    public static SignUpDTORequest createWithEmptyLastName() {
        return new SignUpDTORequest("foo", "", "foobar@gmail.com", "123");
    }

    public static SignUpDTORequest createWithEmptyEmail() {
        return new SignUpDTORequest("foo", "bar", "", "123");
    }

    public static SignUpDTORequest createWithInvalidEmailFormat() {
        return new SignUpDTORequest("foo", "bar", "foobar@", "123");
    }

    public static SignUpDTORequest createWithEmpyPassword() {
        return new SignUpDTORequest("foo", "bar", "foobar@gmail.com", "");
    }
}
