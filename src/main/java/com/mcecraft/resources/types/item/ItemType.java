package com.mcecraft.resources.types.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mcecraft.resources.*;
import com.mcecraft.resources.mojang.DefaultResourcePack;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.item.persistence.ItemPersistenceData;
import com.mcecraft.resources.types.item.persistence.ItemPersistenceStore;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
	public @NotNull ItemResourceBuilder makeBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID) {
		return new ItemResourceBuilder(api, namespaceID, this);
	}

	@Override
	public @NotNull Generator<ItemResource, ItemPersistenceStore> createGenerator(@NotNull ResourceGenerator api, @NotNull PersistenceProvider<ItemPersistenceStore> dataProvider) {
		return new Generator<>() {

			final Map<Material, Set<ItemResource>> resources = new HashMap<>();
			final Map<NamespaceID, Int2ObjectMap<NamespaceID>> materialId2ClaimedCMIs = new HashMap<>();

			{
				ItemPersistenceStore data = dataProvider.getData();
				if (data != null) {
					for (Map.Entry<NamespaceID, ItemPersistenceData> entry : data.data.entrySet()) {
						NamespaceID id = entry.getKey();
						ItemPersistenceData itemData = entry.getValue();

						NamespaceID maybeOld = materialId2ClaimedCMIs
								.computeIfAbsent(itemData.material(), key -> new Int2ObjectOpenHashMap<>())
								.put(itemData.cmi(), id);

						if (maybeOld != null) {
							throw new RuntimeException("Both " + maybeOld.asString() + " and " + id + " are claiming the same item cmi and model (" + itemData.cmi() + " for " + itemData.material().namespace() + ")");
						}
					}
				}
			}

			@Override
			public @NotNull Collection<? extends Resource> dependencies(@NotNull ResourceGenerator api, @NotNull ItemResource resource, @NotNull PersistenceProvider<ItemPersistenceStore> dataProvider) {
				resources.computeIfAbsent(resource.getMaterial(), (key) -> new HashSet<>()).add(resource);

				Set<Resource> includes = new HashSet<>(resource.getIncludes());

				NamespaceID id = resource.getNamespaceID();
				includes.add(
						api.create(IncludeType.INSTANCE, Utils.INTERNAL)
								.data(Loc.of(id, Loc.MODELS), resource.getModel())
								.build(false)
				);

				return includes;
			}

			@Override
			public void generate(@NotNull ResourceGenerator api, @NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<ItemPersistenceStore> dataProvider) {
				ItemPersistenceStore data = dataProvider.getDataOr(ItemPersistenceStore::new);

				for (Map.Entry<Material, Set<ItemResource>> entry : resources.entrySet()) {
					Material material = entry.getKey();
					Set<ItemResource> resources = entry.getValue();

					// get the cmi for each item
					Int2ObjectMap<NamespaceID> claimedCMIs = materialId2ClaimedCMIs.computeIfAbsent(material.namespace(), key -> new Int2ObjectOpenHashMap<>());

					// 0 is the default item
					int cmiCounter = 1;

					for (ItemResource resource : resources) {
						int cmi;

						ItemPersistenceData itemData = data.data.get(resource.getNamespaceID());
						if (itemData != null) {
							cmi = itemData.cmi();
						} else {
							while (claimedCMIs.containsKey(cmiCounter)) {
								cmiCounter++;
							}

							cmi = cmiCounter;
							claimedCMIs.put(cmi, resource.getNamespaceID());

							if (resource.persist()) {
								data.data.put(resource.getNamespaceID(), new ItemPersistenceData(material.namespace(), cmi));
							}
						}
						resource.setCustomModelId(cmi);
					}


					// generate the json files for the client
					NamespaceID namespace = Utils.prefixPath(material.namespace(), "item/");

					JsonObject custom = new JsonObject();

					JsonArray overrides = new JsonArray();

					for (ItemResource itemResource : resources) {
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
