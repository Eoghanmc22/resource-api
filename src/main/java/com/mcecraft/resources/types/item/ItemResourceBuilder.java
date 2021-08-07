package com.mcecraft.resources.types.item;

import com.google.gson.JsonElement;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.ResourceBuilder;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.Utils;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

//Todo
public class ItemResourceBuilder extends ResourceBuilder<ItemResource> {

	private Material material;
	private String modelName;
	private final List<IncludedResourceBuilder> includes = new ArrayList<>();

	private File modelLocation;

	private JsonElement jsonModel;

	protected ItemResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<ItemResource, ?> resourceType) {
		super(namespaceID, resourceType);
	}

	@Override
	protected @NotNull ItemResource buildImpl() {
		if (material == null || modelName == null || (modelLocation == null && jsonModel == null)) {
			throw new NullPointerException("Incomplete builder!");
		}
		if (jsonModel != null) {
			return new ItemResource(getResourceType(), material, modelName, jsonModel, includes);
		} else {
			return new ItemResource(getResourceType(), material, modelName, modelLocation, includes);
		}
	}

	public @NotNull ItemResourceBuilder material(@NotNull Material material) {
		this.material = material;
		return this;
	}

	public @NotNull ItemResourceBuilder modelName(@NotNull String modelName) {
		this.modelName = modelName;
		return this;
	}

	public @NotNull ItemResourceBuilder modelLocation(@NotNull File modelLocation) {
		this.modelLocation = modelLocation;
		return this;
	}

	public @NotNull ItemResourceBuilder jsonModel(@NotNull JsonElement jsonModel) {
		this.jsonModel = jsonModel;
		return this;
	}

	public @NotNull ItemResourceBuilder addInclude(@NotNull UnaryOperator<IncludedResourceBuilder> resource) {
		includes.add(resource.apply(ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)));
		return this;
	}
}
