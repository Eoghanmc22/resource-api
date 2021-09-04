package com.mcecraft.resources.types.item;

import com.google.gson.JsonElement;
import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class ItemResource extends Resource {
	private final @NotNull Material material;
	private final @NotNull String modelName;
	private final @NotNull List<IncludedResourceBuilder> includes;

	private final @Nullable File modelLocation;
	private final @Nullable JsonElement jsonModel;

	public ItemResource(@NotNull ResourceType<? extends Resource, ?> type, @NotNull Material material, @NotNull String modelName, @NotNull File modelLocation, @NotNull List<IncludedResourceBuilder> includes) {
		super(type);

		this.material = material;
		this.modelName = modelName;
		this.includes = includes;

		this.modelLocation = modelLocation;
		this.jsonModel = null;
	}

	public ItemResource(@NotNull ResourceType<? extends Resource, ?> type, @NotNull Material material, @NotNull String modelName, @NotNull JsonElement jsonModel, @NotNull List<IncludedResourceBuilder> includes) {
		super(type);

		this.material = material;
		this.modelName = modelName;
		this.includes = includes;

		this.jsonModel = jsonModel;
		this.modelLocation = null;
	}

	public @NotNull Material getMaterial() {
		return material;
	}

	public @NotNull String getModelName() {
		return modelName;
	}

	public @Nullable File getModelLocation() {
		return modelLocation;
	}

	public @Nullable JsonElement getJsonModel() {
		return jsonModel;
	}

	public @NotNull List<IncludedResourceBuilder> getIncludes() {
		return includes;
	}

}
