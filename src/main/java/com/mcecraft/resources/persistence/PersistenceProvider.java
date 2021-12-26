package com.mcecraft.resources.persistence;

import org.jetbrains.annotations.Nullable;

public class PersistenceProvider<P extends PersistenceStore> {

    private @Nullable P data;

    public PersistenceProvider(@Nullable P data) {
        this.data = data;
    }

    public @Nullable P getData() {
        return data;
    }

    public void setData(@Nullable P data) {
        this.data = data;
    }
}
