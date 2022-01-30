package com.mcecraft.resources.types.item;

import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.include.IncludedResource;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Include;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ItemResourceBuilder extends ResourceBuilder<ItemResource> {

	private Material material;
	private final List<IncludedResource> includes = new ArrayList<>();
	private Data model;

	private boolean persist = true;

	protected ItemResourceBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID, @NotNull ResourceType<ItemResource, ?, ?> resourceType) {
		super(api, resourceType, namespaceID);
	}

	@Override
	protected @NotNull ItemResource buildImpl() {
		if (material == null || model == null) {
			throw new NullPointerException("Incomplete builder!");
		}

		return new ItemResource(getResourceApi(), getResourceType(), getNamespaceID(), material, model, includes, persist);
	}

	public @NotNull ItemResourceBuilder material(@NotNull Material material) {
		this.material = material;
		return this;
	}

	public @NotNull ItemResourceBuilder model(@NotNull Data model, @NotNull Include... includes) {
		this.model = model;

		if (includes != null && includes.length > 0) {
			include(includedResourceBuilder -> includedResourceBuilder.include(includes));
		}

		return this;
	}

	public @NotNull ItemResourceBuilder include(@NotNull UnaryOperator<@NotNull IncludedResourceBuilder> resource) {
		includes.add(resource.apply(getResourceApi().create(IncludeType.INSTANCE, Utils.INTERNAL)).build(false));
		return this;
	}

	public @NotNull ItemResourceBuilder persist(boolean persist) {
		this.persist = persist;
		return this;
	}
}
