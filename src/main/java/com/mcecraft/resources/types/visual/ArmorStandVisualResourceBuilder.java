package com.mcecraft.resources.types.visual;

import com.mcecraft.resources.ResourceBuilder;
import com.mcecraft.resources.ResourceGenerator;
import com.mcecraft.resources.ResourceType;
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

public class ArmorStandVisualResourceBuilder extends ResourceBuilder<ArmorStandVisualResource> {

    private final @NotNull ItemResourceBuilder item = getResourceApi().create(ItemType.INSTANCE, getNamespaceID()).material(Material.PAPER).persist(false);

    private Data model;
    private boolean isSmall = false;

    protected ArmorStandVisualResourceBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID, @NotNull ResourceType<ArmorStandVisualResource, ?, ?> resourceType) {
        super(api, resourceType, namespaceID);
    }

    @Override
    protected @NotNull ArmorStandVisualResource buildImpl() {
        // inject the correct display settings
        item.model(Json.lazy(() -> Utils.mergeJson(Json.data(model).json(), isSmall ? ArmorStandVisualType.DISPLAY_SETTINGS_SMALL : ArmorStandVisualType.DISPLAY_SETTINGS)));

        // Completion checks in item builder
        return new ArmorStandVisualResource(getResourceApi(), getResourceType(), getNamespaceID(), item.build(false), isSmall);
    }

    public @NotNull ArmorStandVisualResourceBuilder model(@NotNull Data model, @NotNull Include... includes) {
        this.model = model;

        if (includes != null && includes.length > 0) {
            include(includedResourceBuilder -> includedResourceBuilder.include(includes));
        }
        return this;
    }

    public @NotNull ArmorStandVisualResourceBuilder small(boolean isSmall) {
        this.isSmall = isSmall;
        return this;
    }

    public @NotNull ArmorStandVisualResourceBuilder include(@NotNull UnaryOperator<IncludedResourceBuilder> resource) {
        item.include(resource);
        return this;
    }
}
