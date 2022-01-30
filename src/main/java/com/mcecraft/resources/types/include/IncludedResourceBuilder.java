package com.mcecraft.resources.types.include;

import com.mcecraft.resources.ResourceGenerator;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.ResourceBuilder;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.utils.Include;
import com.mcecraft.resources.utils.Loc;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class IncludedResourceBuilder extends ResourceBuilder<IncludedResource> {

	private final Map<Loc, Data> resources = new HashMap<>();

	protected IncludedResourceBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID, @NotNull ResourceType<IncludedResource, ?, ?> resourceType) {
		super(api, resourceType, namespaceID);
	}

	@Override
	protected @NotNull IncludedResource buildImpl() {
		if (resources.isEmpty()) {
			throw new NullPointerException("Incomplete builder!");
		}

		return new IncludedResource(getResourceApi(), getResourceType(), getNamespaceID(), resources);
	}

	public @NotNull IncludedResourceBuilder data(@NotNull Loc loc, @NotNull Data data) {
		resources.put(loc, data);
		return this;
	}

	public @NotNull IncludedResourceBuilder file(@NotNull Loc loc, @NotNull Path file) {
		return data(loc, Data.path(file));
	}

	public @NotNull IncludedResourceBuilder text(@NotNull Loc loc, @NotNull String text) {
		return data(loc, Data.str(text));
	}

	public @NotNull IncludedResourceBuilder include(Include @NotNull... includes) {
		for (Include include : includes) {
			data(include.loc(), include.data());
		}
		return this;
	}

}
