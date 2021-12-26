package com.mcecraft.resources;

import com.mcecraft.resources.persistence.PersistenceStore;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResourceType<R extends Resource, B extends ResourceBuilder<R>, P extends PersistenceStore> {

    default @Nullable NamespaceID getPersistenceId() {
        return null;
    }


    @NotNull B makeBuilder(@NotNull NamespaceID namespaceID);

    @NotNull Generator<R, P> createGenerator();
}
