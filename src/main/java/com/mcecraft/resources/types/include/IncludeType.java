package com.mcecraft.resources.types.include;

import com.mcecraft.resources.GeneratedResourcePack;
import com.mcecraft.resources.Generator;
import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
			public void generate(@NotNull GeneratedResourcePack rp) {
				for (IncludedResource resource : resources) {
					if (resource.getPath() != null) {
						rp.includeFile(resource.getDestPath(), resource.getPath());
					} else if (resource.getText() != null) {
						rp.include(resource.getDestPath(), resource.getText());
					}
				}
			}
		};
	}

}
