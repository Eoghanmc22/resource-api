package com.mcecraft.resources.types.block.spawner;

import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import com.mcecraft.resources.types.item.ItemResourceBuilder;
import com.mcecraft.resources.types.item.ItemType;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class SpawnerBlockResourceBuilder extends ResourceBuilder<SpawnerBlockResource> {

    private final @NotNull ItemResourceBuilder item = ResourceApi.create(ItemType.INSTANCE, getNamespaceID()).material(Material.PAPER);

    protected SpawnerBlockResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<SpawnerBlockResource, ?, ?> resourceType) {
        super(resourceType, namespaceID);
    }

    @Override
    protected @NotNull SpawnerBlockResource buildImpl() {
        return new SpawnerBlockResource(getResourceType(), getNamespaceID(), item.build(false));
    }

    public SpawnerBlockResourceBuilder model(@NotNull Data model) {
        // inject the correct display settings
        item.model(Json.lazy(() -> Utils.mergeJson(Json.of(model).json(), SpawnerBlockType.DISPLAY_SETTINGS)));
        return this;
    }

    public SpawnerBlockResourceBuilder include(@NotNull UnaryOperator<IncludedResourceBuilder> resource) {
        item.include(resource);
        return this;
    }

    public SpawnerBlockResourceBuilder persist(boolean persist) {
        item.persist(persist);
        return this;
    }
}
