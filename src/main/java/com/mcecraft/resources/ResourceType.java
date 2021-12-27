package com.mcecraft.resources;

import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.persistence.PersistenceStore;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResourceType<R extends Resource, B extends ResourceBuilder<R>, P extends PersistenceStore> {

    default @Nullable NamespaceID getPersistenceId() {
        return null;
    }


    @NotNull B makeBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID);

    default @NotNull Generator<R, P> __createGenerator(@NotNull ResourceGenerator api, @NotNull PersistenceProvider<?> dataProvider) { return createGenerator(api, (PersistenceProvider<P>) dataProvider); }
    @NotNull Generator<R, P> createGenerator(@NotNull ResourceGenerator api, @NotNull PersistenceProvider<P> dataProvider);
}
