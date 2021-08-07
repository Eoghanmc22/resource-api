package com.mcecraft.resources;

import org.jetbrains.annotations.NotNull;

public abstract class Resource {
	private final @NotNull ResourceType<? extends Resource, ?> resourceType;

	public Resource(@NotNull ResourceType<? extends Resource, ?> resourceType) {
		this.resourceType = resourceType;
	}

	public @NotNull ResourceType<? extends Resource, ?> getResourceType() {
		return resourceType;
	}

}
