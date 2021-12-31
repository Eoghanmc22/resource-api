package com.mcecraft.resources.types.visual;

import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceGenerator;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.types.item.ItemResource;
import net.minestom.server.entity.Entity;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class ArmorStandVisualResource extends Resource {
    private final ItemResource item;
    private final boolean isSmall;

    public ArmorStandVisualResource(@NotNull ResourceGenerator api, @NotNull ResourceType<? extends Resource, ?, ?> resourceType, @NotNull NamespaceID namespaceID, @NotNull ItemResource item, boolean isSmall) {
        super(api, resourceType, namespaceID);
        this.item = item;
        this.isSmall = isSmall;
    }

    public @NotNull ItemResource getItem() {
        return item;
    }

    public boolean isSmall() {
        return isSmall;
    }

    /**
     * Created the entity that displays item model
     * <p>
     * This item model will already be scaled to correct size.
     * Meaning, it will be displayed at the size block, not the normal size of a block on a armor stand's head
     * <p>
     * You have to spawn this entity into an instance using the Entity#setInstance(Instance, Pos) method
     * <p>
     * Additionally depending on the size set, you will need to add
     * <code>ArmorStandVisualType.SPAWN_OFFSET</code> (isSmall = false) or
     * <code>ArmorStandVisualType.SPAWN_OFFSET_SMALL</code> (isSmall = true)
     * to get the armor stand to display at the right height
     *
     * @return An entity that renders as the chosen model
     */
    public @NotNull Entity createEntity() {
        return new DisplayEntity(item.createItemStack(), isSmall);
    }
}
