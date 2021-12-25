package com.mcecraft.resources.types.block.spawner;

import com.google.gson.JsonElement;
import com.mcecraft.resources.*;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SpawnerBlockType implements ResourceType<SpawnerBlockResource, SpawnerBlockResourceBuilder> {

    public static final SpawnerBlockType INSTANCE = new SpawnerBlockType();

    private SpawnerBlockType() {}

    static final JsonElement DISPLAY_SETTINGS = Utils.fromJsonReader(Utils.resourceReader("/armorstand_block.json"));

    @Override
    public @NotNull SpawnerBlockResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
        return new SpawnerBlockResourceBuilder(namespaceID, this);
    }

    @Override
    public @NotNull Generator<SpawnerBlockResource> createGenerator() {
        return new Generator<>() {
            @Override
            public @NotNull Collection<? extends Resource> dependencies(@NotNull SpawnerBlockResource resource) {
                return Collections.singleton(resource.getItem());
            }

            @Override
            public void generate(@NotNull DynamicResourcePack rp) {
                rp.include(Loc.of(NamespaceID.from("resource_api", "clear"), Loc.TEXTURES), Data.ofResource("/clear.png"));
                rp.include(Loc.of(NamespaceID.from("block/spawner"), Loc.MODELS), Data.ofResource("/hide_spawner.json"));
            }
        };
    }
}
