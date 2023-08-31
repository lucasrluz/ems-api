package com.emsapi.services.util;

public class EmailAlreadyRegisteredException extends Exception {
    public EmailAlreadyRegisteredException() {
        super("Email already registered");
    }
}
