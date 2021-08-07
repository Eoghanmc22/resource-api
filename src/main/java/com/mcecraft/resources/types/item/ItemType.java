package com.mcecraft.resources.types.item;

import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

//Todo
public class ItemType implements ResourceType<ItemResource, ItemResourceBuilder> {

	public static final ItemType INSTANCE = new ItemType();

	private ItemType() {}

	@Override
	public @NotNull ItemResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
		return new ItemResourceBuilder(namespaceID, this);
	}

	@Override
	public @NotNull Generator createGenerator() {
		return new Generator() {

			Set<ItemResource> resources = new HashSet<>();

			@Override
			public @Nullable Set<Resource> add(@NotNull Object resource1) {
				final ItemResource resource = (ItemResource) resource1;
				resources.add(resource);

				Set<Resource> includes = new HashSet<>();

				for (IncludedResourceBuilder include : resource.getIncludes()) {
					includes.add(include.build(false));
				}

				//todo generate the item's model (prob in generate method) and add custom model data and make it persist
				if (resource.getJsonModel() != null) {
					includes.add(ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL).json())
				}

				return includes;
			}

			@Override
			public void generate(@NotNull GeneratedResourcePack rp) {
				for (ItemResource resource : resources) {

				}
			}
		};
	}

}
