package com.mcecraft.resources.types.block.spawner;

import com.google.gson.JsonElement;
import com.mcecraft.resources.*;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class SpawnerBlockType implements ResourceType<SpawnerBlockResource, SpawnerBlockResourceBuilder> {

    public static final SpawnerBlockType INSTANCE = new SpawnerBlockType();

    private SpawnerBlockType() {}

    static final JsonElement DISPLAY_SETTINGS = Utils.json(Utils.resourceReader("/armorstand_block.json"));

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
            public void generate(@NotNull GeneratedResourcePack rp) {
                try {
                    rp.include(Utils.resourcePath(NamespaceID.from("resource_api", "clear"), Utils.TEXTURES), Utils.resourceInputStream("/clear.png").readAllBytes());
                    rp.include(Utils.resourcePath(NamespaceID.from("block/spawner"), Utils.MODELS), Utils.resourceInputStream("/hide_spawner.json").readAllBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
