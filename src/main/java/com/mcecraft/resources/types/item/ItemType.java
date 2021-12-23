package com.mcecraft.resources.types.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludeType;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemType implements ResourceType<ItemResource, ItemResourceBuilder> {

	public static final ItemType INSTANCE = new ItemType();

	private ItemType() {}

	@Override
	public @NotNull ItemResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
		return new ItemResourceBuilder(namespaceID, this);
	}

	@Override
	public @NotNull Generator<ItemResource> createGenerator() {
		return new Generator<>() {

			final Map<Material, Set<ItemResource>> resources = new HashMap<>();

			@Override
			public @NotNull Collection<? extends Resource> dependencies(@NotNull ItemResource resource) {
				resources.computeIfAbsent(resource.getMaterial(), (key) -> new HashSet<>()).add(resource);

				Set<Resource> includes = new HashSet<>(resource.getIncludes());

				NamespaceID id = resource.getNamespaceID();
				includes.add(
						ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)
								.json(
										Utils.resourcePath(id, Utils.MODELS),
										resource.getModelProvider().get()
								)
								.build(false)
				);

				return includes;
			}

			@Override
			public void generate(@NotNull GeneratedResourcePack rp) {
				for (Map.Entry<Material, Set<ItemResource>> entry : resources.entrySet()) {
					Material material = entry.getKey();
					Set<ItemResource> resources = entry.getValue();
					// 0 is the default item
					int cmiCounter = 1;
					NamespaceID namespace = Utils.prefixPath(material.namespace(), "item/");

					JsonObject json = new JsonObject();

					JsonArray overrides = new JsonArray();
					for (ItemResource itemResource : resources) {
						itemResource.setCustomModelId(cmiCounter);

						JsonObject override = new JsonObject();

						JsonObject predicate = new JsonObject();
						predicate.add("custom_model_data", new JsonPrimitive(cmiCounter++));
						override.add("predicate", predicate);

						override.add("model", new JsonPrimitive(itemResource.getNamespaceID().asString()));

						overrides.add(override);
					}
					json.add("overrides", overrides);

					rp.include(Utils.resourcePath(namespace, Utils.MODELS), json);
				}
			}
		};
	}

}
