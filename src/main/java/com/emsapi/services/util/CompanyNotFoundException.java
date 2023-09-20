package com.emsapi.services.util;

public class CompanyNotFoundException extends Exception {
    public CompanyNotFoundException() {
        super("Company not found");
    }
}
