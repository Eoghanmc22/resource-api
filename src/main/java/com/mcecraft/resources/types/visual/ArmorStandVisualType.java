package com.mcecraft.resources.types.visual;

import com.google.gson.JsonElement;
import com.mcecraft.resources.*;
import com.mcecraft.resources.persistence.NullPersistenceStore;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class ArmorStandVisualType implements ResourceType<ArmorStandVisualResource, ArmorStandVisualResourceBuilder, NullPersistenceStore> {

    public static final ArmorStandVisualType INSTANCE = new ArmorStandVisualType();

    private ArmorStandVisualType() {}

    static final JsonElement DISPLAY_SETTINGS = Utils.fromJsonReader(Utils.resourceReader("/armorstand_visual.json"));
    static final JsonElement DISPLAY_SETTINGS_SMALL = Utils.fromJsonReader(Utils.resourceReader("/armorstand_visual_small.json"));

    public static final Pos SPAWN_OFFSET = new Pos(0, -1.1875, 0);
    public static final Pos SPAWN_OFFSET_SMALL = new Pos(0, -0.4, 0);

    @Override
    public @NotNull ArmorStandVisualResourceBuilder makeBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID) {
        return new ArmorStandVisualResourceBuilder(api, namespaceID, this);
    }

    @Override
    public @NotNull Generator<ArmorStandVisualResource, NullPersistenceStore> createGenerator(@NotNull ResourceGenerator api, @NotNull PersistenceProvider<NullPersistenceStore> dataProvider) {
        return new Generator<>() {
            @Override
            public @NotNull Collection<? extends Resource> dependencies(@NotNull ResourceGenerator api, @NotNull ArmorStandVisualResource resource, @NotNull PersistenceProvider<NullPersistenceStore> persistenceData) {
                return Collections.singleton(resource.getItem());
            }

            @Override
            public void generate(@NotNull ResourceGenerator api, @NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<NullPersistenceStore> persistenceData) {
                // everything in handled in item resource
            }
        };
    }
}
