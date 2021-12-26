package com.mcecraft.resources.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Once<T> implements Supplier<T> {

    private final Supplier<T> supplier;
    private @Nullable T val = null;

    public Once(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public @NotNull T get() {
        if (val != null) {
            return val;
        } else {
            return val = supplier.get();
        }
    }
}
