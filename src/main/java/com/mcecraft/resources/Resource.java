package com.mcecraft.resources;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public abstract class Resource {
	private final ResourceType<? extends Resource, ?, ?> resourceType;
	private final NamespaceID namespaceID;

	public Resource(@NotNull ResourceType<? extends Resource, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
		this.resourceType = resourceType;
		this.namespaceID = namespaceID;
	}

	public @NotNull ResourceType<? extends Resource, ?, ?> getResourceType() {
		return resourceType;
	}

	public @NotNull NamespaceID getNamespaceID() {
		return namespaceID;
	}
}
