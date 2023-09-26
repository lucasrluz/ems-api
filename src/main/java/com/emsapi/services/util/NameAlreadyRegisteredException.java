package com.emsapi.services.util;

public class NameAlreadyRegisteredException extends Exception {
    public NameAlreadyRegisteredException() {
        super("Name already registered");
    }
}
