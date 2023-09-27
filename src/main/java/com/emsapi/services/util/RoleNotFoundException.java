package com.emsapi.services.util;

public class RoleNotFoundException extends Exception {
    public RoleNotFoundException() {
        super("Role not found");
    }
}
