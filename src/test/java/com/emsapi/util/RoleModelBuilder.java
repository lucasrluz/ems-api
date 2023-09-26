package com.emsapi.util;

import java.util.UUID;

import com.emsapi.models.RoleModel;

public abstract class RoleModelBuilder {
    public static RoleModel createWithRoleId() {
        return new RoleModel(UUID.randomUUID(), "foo");
    }

    public static RoleModel createWithEmptyRoleId() {
        return new RoleModel("foo");
    }
}
