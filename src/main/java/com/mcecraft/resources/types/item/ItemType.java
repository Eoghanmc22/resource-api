package com.mcecraft.resources.types.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcecraft.resources.*;
import com.mcecraft.resources.mojang.DefaultResourcePack;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.item.persistence.ItemPersistenceStore;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemType implements ResourceType<ItemResource, ItemResourceBuilder, ItemPersistenceStore> {

	public static final ItemType INSTANCE = new ItemType();

	private ItemType() {}

	@Override
	public @Nullable NamespaceID getPersistenceId() {
		return NamespaceID.from("resource_api:item");
	}

	@Override
	public @NotNull ItemResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
		return new ItemResourceBuilder(namespaceID, this);
	}

	@Override
	public @NotNull Generator<ItemResource, ItemPersistenceStore> createGenerator() {
		return new Generator<>() {

			final Map<Material, Set<ItemResource>> resources = new HashMap<>();

			@Override
			public @NotNull Collection<? extends Resource> dependencies(@NotNull ItemResource resource, @NotNull PersistenceProvider<ItemPersistenceStore> dataProvider) {
				resources.computeIfAbsent(resource.getMaterial(), (key) -> new HashSet<>()).add(resource);

				Set<Resource> includes = new HashSet<>(resource.getIncludes());

				NamespaceID id = resource.getNamespaceID();
				includes.add(
						ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)
								.data(Loc.of(id, Loc.MODELS), resource.getModel())
								.build(false)
				);

				return includes;
			}

			@Override
			public void generate(@NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<ItemPersistenceStore> dataProvider) {
				for (Map.Entry<Material, Set<ItemResource>> entry : resources.entrySet()) {
					Material material = entry.getKey();
					Set<ItemResource> resources = entry.getValue();
					// 0 is the default item
					int cmiCounter = 1;
					NamespaceID namespace = Utils.prefixPath(material.namespace(), "item/");

					JsonObject custom = new JsonObject();

					JsonArray overrides = new JsonArray();

					for (ItemResource itemResource : resources) {
						itemResource.setCustomModelId(cmiCounter);

						JsonObject override = new JsonObject();

						JsonObject predicate = new JsonObject();
						predicate.add("custom_model_data", new JsonPrimitive(cmiCounter++));
						override.add("predicate", predicate);

						override.add("model", Utils.toJsonTree(itemResource.getNamespaceID().asString()));

						overrides.add(override);
					}
					custom.add("overrides", overrides);

					Loc resourceLocation = Loc.of(namespace, Loc.MODELS);

					JsonElement vanilla = Json.of(DefaultResourcePack.get(resourceLocation)).json();

					rp.include(resourceLocation, Json.of(Utils.mergeJson(vanilla, custom)));
				}
			}
		};
	}

}
