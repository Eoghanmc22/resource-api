package com.mcecraft.resources.persistence;

import com.mcecraft.resources.ResourceType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class GlobalPersistenceStore {
    private final Map<NamespaceID, PersistenceStore> store = new HashMap<>();

    public <P extends PersistenceStore> PersistenceProvider<P> get(ResourceType<?, ?, P> resourceType) {
        NamespaceID id = resourceType.getPersistenceId();

        if (id == null) {
            return new PersistenceProvider<>(null);
        }

        return new PersistenceProvider<>((P) store.get(id));
    }

    public <P extends PersistenceStore> void store(ResourceType<?, ?, P> resourceType, @NotNull PersistenceProvider<P> provider) {
        NamespaceID id = resourceType.getPersistenceId();
        P data = provider.getData();

        if (id == null) {
            return;
        }

        store.put(id, data);
    }
}
