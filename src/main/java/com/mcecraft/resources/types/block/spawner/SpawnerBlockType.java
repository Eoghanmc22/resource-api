package com.mcecraft.resources.types.block.spawner;

import com.google.gson.JsonElement;
import com.mcecraft.resources.*;
import com.mcecraft.resources.persistence.NullPersistenceStore;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class SpawnerBlockType implements ResourceType<SpawnerBlockResource, SpawnerBlockResourceBuilder, NullPersistenceStore> {

    public static final SpawnerBlockType INSTANCE = new SpawnerBlockType();

    private SpawnerBlockType() {}

    static final JsonElement DISPLAY_SETTINGS = Utils.fromJsonReader(Utils.resourceReader("/armorstand_block.json"));

    // Persistence is handled by ItemResource

    @Override
    public @NotNull SpawnerBlockResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
        return new SpawnerBlockResourceBuilder(namespaceID, this);
    }

    @Override
    public @NotNull Generator<SpawnerBlockResource, NullPersistenceStore> createGenerator(@NotNull PersistenceProvider<NullPersistenceStore> _store) {
        return new Generator<>() {
            @Override
            public @NotNull Collection<? extends Resource> dependencies(@NotNull SpawnerBlockResource resource, @NotNull PersistenceProvider<NullPersistenceStore> _store) {
                return Collections.singleton(resource.getItem());
            }

            @Override
            public void generate(@NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<NullPersistenceStore> _store) {
                rp.include(Loc.of(NamespaceID.from("resource_api", "clear"), Loc.TEXTURES), Data.ofResource("/clear.png"));
                rp.include(Loc.of(NamespaceID.from("block/spawner"), Loc.MODELS), Data.ofResource("/hide_spawner.json"));
            }
        };
    }
}
