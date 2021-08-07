package com.mcecraft.resources.types.include;

import com.mcecraft.resources.GeneratedResourcePack;
import com.mcecraft.resources.Generator;
import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class IncludeType implements ResourceType<IncludedResource, IncludedResourceBuilder> {

	public static final IncludeType INSTANCE = new IncludeType();

	private IncludeType() {
	}

	@Override
	public @NotNull IncludedResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
		return new IncludedResourceBuilder(namespaceID, this);
	}

	@Override
	public @NotNull Generator createGenerator() {
		return new Generator() {

			Set<IncludedResource> resources = new HashSet<>();

			@Override
			public @Nullable Set<Resource> add(@NotNull Object resource) {
				resources.add((IncludedResource) resource);
				return null;
			}

			@Override
			public void generate(@NotNull GeneratedResourcePack rp) {
				for (IncludedResource resource : resources) {
					if (resource.getFile() != null) {
						rp.includeFile(resource.getDestPath(), resource.getFile());
					} else if (resource.getText() != null) {
						rp.include(resource.getDestPath(), resource.getText());
					}
				}
			}
		};
	}

}
