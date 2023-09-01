package com.emsapi.services.util;

public class EmailOrPasswordInvalidException extends Exception {
    public EmailOrPasswordInvalidException() {
        super("Email or password invalid");
    }
}
