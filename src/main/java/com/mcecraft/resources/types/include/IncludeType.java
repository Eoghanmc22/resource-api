package com.mcecraft.resources.types.include;

import com.mcecraft.resources.*;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IncludeType implements ResourceType<IncludedResource, IncludedResourceBuilder> {

	public static final IncludeType INSTANCE = new IncludeType();

	private IncludeType() {}

	@Override
	public @NotNull IncludedResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
		return new IncludedResourceBuilder(namespaceID, this);
	}

	@Override
	public @NotNull Generator<IncludedResource> createGenerator() {
		return new Generator<>() {

			private final Set<IncludedResource> resources = new HashSet<>();

			@Override
			public @NotNull Collection<? extends Resource> dependencies(@NotNull IncludedResource resource) {
				resources.add(resource);
				return Collections.emptyList();
			}

			@Override
			public void generate(@NotNull ResourcePack rp) {
				for (IncludedResource resource : resources) {
					for (Map.Entry<Loc, Data> entry : resource.getResources().entrySet()) {
						Loc loc = entry.getKey();
						Data data = entry.getValue();

						rp.include(loc, data);
					}
				}
			}
		};
	}

}
