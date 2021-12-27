package com.mcecraft.resources.types.item;

import com.mcecraft.resources.ResourceGenerator;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.types.include.IncludedResource;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemResource extends Resource {
	private final @NotNull Material material;
	private final @NotNull List<@NotNull IncludedResource> includes;
	private final @NotNull Data model;

	private final boolean persist;

	private int customModelId = 0;

	public ItemResource(@NotNull ResourceGenerator api, @NotNull ResourceType<? extends Resource, ?, ?> type, @NotNull NamespaceID namespaceID, @NotNull Material material, @NotNull Data model, @NotNull List<@NotNull IncludedResource> includes, boolean persist) {
		super(api, type, namespaceID);

		this.material = material;
		this.includes = includes;
		this.model = model;

		this.persist = persist;
	}

	public @NotNull Material getMaterial() {
		return material;
	}

	public @NotNull Data getModel() {
		return model;
	}

	public @NotNull List<@NotNull IncludedResource> getIncludes() {
		return includes;
	}

	public boolean persist() {
		return persist;
	}

	/**
	 * IMPORTANT: this is only set after the resource pack has been generated other wise, it is 0
	 *
	 * @return the item's custom model id
	 */
	public int getCustomModelId() {
		return customModelId;
	}

	void setCustomModelId(int customModelId) {
		this.customModelId = customModelId;
	}

	public @NotNull ItemStack createItemStack() {
		if (customModelId == 0) {
			throw new RuntimeException("The resource pack has not been generated yet");
		}

		return ItemStack.builder(material).meta((meta) -> meta.customModelData(customModelId)).build();
	}
}
