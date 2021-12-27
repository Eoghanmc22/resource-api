package com.mcecraft.resources.persistence;

import com.mcecraft.resources.ResourceType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GlobalPersistenceStore {
    private final Map<NamespaceID, PersistenceStore> store = new HashMap<>();

    public <P extends PersistenceStore> @NotNull PersistenceProvider<P> get(@NotNull ResourceType<?, ?, P> resourceType) {
        NamespaceID id = resourceType.getPersistenceId();

        if (id == null) {
            return new PersistenceProvider<>(null, null, null);
        }

        return new PersistenceProvider<>((P) store.get(id), id, store);
    }
}
