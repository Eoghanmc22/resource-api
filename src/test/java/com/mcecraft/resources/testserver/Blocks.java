package com.mcecraft.resources.testserver;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.utils.Include;
import com.mcecraft.resources.types.block.BlockResource;
import com.mcecraft.resources.types.block.real.replacement.BlockReplacement;
import com.mcecraft.resources.types.block.real.RealBlockResourceType;
import com.mcecraft.resources.types.block.spawner.SpawnerBlockType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Blocks {

    public static final Map<NamespaceID, BlockResource> REGISTRY = new HashMap<>();

    public static final BlockResource TEST = ResourceApi.create(SpawnerBlockType.INSTANCE, NamespaceID.from("demo:test_block"))
            .model(
                    Data.path("resources/models/demo_block.json"),
                    Include.tex("demo:texture2", "resources/textures/texture2.png")
            )
            .build();

    public static final BlockResource TEST2 = ResourceApi.create(RealBlockResourceType.INSTANCE, NamespaceID.from("demo:test_block2"))
            .model(
                    Data.path("resources/models/demo_block2.json"),
                    Include.tex("demo:texture3", "resources/textures/texture3.png")
            )
            .blockReplacement(BlockReplacement.NOTE_BLOCK)
            .build();


    public static void init() {
        if (REGISTRY.isEmpty()) {
            add(TEST);
            add(TEST2);
        }
    }

    private static void add(@NotNull BlockResource block) {
        REGISTRY.put(block.getNamespaceID(), block);
    }

}
