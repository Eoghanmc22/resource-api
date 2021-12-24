package com.mcecraft.resources.types.include;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.ResourceBuilder;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.utils.Loc;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class IncludedResourceBuilder extends ResourceBuilder<IncludedResource> {

	private final Map<Loc, Data> resources = new HashMap<>();

	protected IncludedResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<IncludedResource, ?> resourceType) {
		super(resourceType, namespaceID);
	}

	@Override
	protected @NotNull IncludedResource buildImpl() {
		if (resources.isEmpty()) {
			throw new NullPointerException("Incomplete builder!");
		}

		return new IncludedResource(getResourceType(), getNamespaceID(), resources);
	}

	public @NotNull IncludedResourceBuilder file(@NotNull Loc loc, @NotNull Path file) {
		resources.put(loc, Data.of(file));

		return this;
	}

	public @NotNull IncludedResourceBuilder data(@NotNull Loc loc, @NotNull Data data) {
		resources.put(loc, data);

		return this;
	}

	public @NotNull IncludedResourceBuilder text(@NotNull Loc loc, @NotNull String text) {
		resources.put(loc, Data.of(text));

		return this;
	}

}
