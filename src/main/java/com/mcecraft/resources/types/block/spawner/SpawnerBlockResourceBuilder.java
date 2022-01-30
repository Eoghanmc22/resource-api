package com.mcecraft.resources.types.block.spawner;

import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import com.mcecraft.resources.types.item.ItemResourceBuilder;
import com.mcecraft.resources.types.item.ItemType;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Include;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public class SpawnerBlockResourceBuilder extends ResourceBuilder<SpawnerBlockResource> {

    private final @NotNull ItemResourceBuilder item = getResourceApi().create(ItemType.INSTANCE, getNamespaceID()).material(Material.PAPER);

    protected SpawnerBlockResourceBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID, @NotNull ResourceType<SpawnerBlockResource, ?, ?> resourceType) {
        super(api, resourceType, namespaceID);
    }

    @Override
    protected @NotNull SpawnerBlockResource buildImpl() {
        // Completion checks in item builder
        return new SpawnerBlockResource(getResourceApi(), getResourceType(), getNamespaceID(), item.build(false));
    }

    public @NotNull SpawnerBlockResourceBuilder model(@NotNull Data model, @NotNull Include... includes) {
        // inject the correct display settings
        item.model(Json.lazy(() -> Utils.mergeJson(Json.data(model).json(), SpawnerBlockType.DISPLAY_SETTINGS)));

        if (includes != null && includes.length > 0) {
            include(includedResourceBuilder -> includedResourceBuilder.include(includes));
        }

        return this;
    }

    public @NotNull SpawnerBlockResourceBuilder include(@NotNull UnaryOperator<IncludedResourceBuilder> resource) {
        item.include(resource);
        return this;
    }

    public @NotNull SpawnerBlockResourceBuilder persist(boolean persist) {
        item.persist(persist);
        return this;
    }
}
