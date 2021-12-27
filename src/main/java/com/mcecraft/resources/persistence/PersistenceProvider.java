package com.mcecraft.resources.persistence;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class PersistenceProvider<P extends PersistenceStore> {

    private @Nullable P data;
    private final NamespaceID id;
    private final Map<NamespaceID, PersistenceStore> globalStore;

    public PersistenceProvider(@Nullable P data, @Nullable NamespaceID id, @Nullable Map<NamespaceID, PersistenceStore> globalStore) {
        this.data = data;
        this.id = id;
        this.globalStore = globalStore;
    }

    public @Nullable P getData() {
        return data;
    }

    public @NotNull P getDataOr(@NotNull Supplier<@NotNull P> or) {
        if (data == null) {
            setData(or.get());
            return data;
        }
        return data;
    }

    public void setData(@Nullable P data) {
        this.data = data;

        if (id == null || globalStore == null) {
            throw new RuntimeException("For data to be stored persistently, a persistence id needs to be set by overriding getPersistenceId() in your implementation of ResourceType");
        }

        globalStore.put(id, data);
    }
}
