package com.umar.apps.concurrent.threadlocal;

import java.util.Objects;

public record Context(String username) {

    public Context {
        Objects.requireNonNull(username, "username is required");
    }

    public static Context of(String username) {
        return new Context(username);
    }
}
