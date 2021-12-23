package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public interface ResourceType<R extends Resource, B extends ResourceBuilder<R>> {

    @NotNull B makeBuilder(@NotNull NamespaceID namespaceID);

    @NotNull Generator<R> createGenerator();
}
