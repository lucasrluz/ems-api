package com.emsapi.util;

import java.util.UUID;

import com.emsapi.models.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class UserModelBuilder {
    public static UserModel createWithEmptyUserId() {
        return new UserModel("foo", "bar", "foobar@gmail.com", "123");
    }

    public static UserModel createWithUserId() {
        return new UserModel(UUID.randomUUID(), "foo", "bar", "foobar@gmail.com", BCrypt.withDefaults().hashToString(12, "123".toCharArray()));
    }
}
