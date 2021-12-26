package com.mcecraft.resources.types.include;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.utils.Loc;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public class IncludedResource extends Resource {

	private final Map<Loc, Data> resources;

	public IncludedResource(@NotNull ResourceType<? extends Resource, ?, ?> type, @NotNull NamespaceID namespaceID, @NotNull Map<Loc, Data> resources) {
		super(type, namespaceID);

		this.resources = Collections.unmodifiableMap(resources);
	}

	public @NotNull Map<Loc, Data> getResources() {
		return resources;
	}
}
