package com.mcecraft.resources.testserver.blocks;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.types.block.BlockResource;
import com.mcecraft.resources.types.block.real.replacement.BlockReplacement;
import com.mcecraft.resources.types.block.real.RealBlockResourceType;
import com.mcecraft.resources.types.block.spawner.SpawnerBlockType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Blocks {

    private final BlockResource blockResource;

    public Blocks(BlockResource itemResource) {
        this.blockResource = itemResource;
    }

    public BlockResource getBlockResource() {
        return blockResource;
    }

    public Block create() {
        return blockResource.createBlock();
    }



    public static final Map<NamespaceID, Blocks> REGISTRY = new HashMap<>();

    public static final Blocks TEST = new Blocks(ResourceApi.create(SpawnerBlockType.INSTANCE, NamespaceID.from("demo:test_block"))
            .model(Data.of(Path.of("resources/models/demo_block.json")))
            .include(b -> b.file(Loc.of(NamespaceID.from("demo:texture2"), Loc.TEXTURES), Path.of("resources/textures/texture2.png")))
            .build()
    );

    public static final Blocks TEST2 = new Blocks(ResourceApi.create(RealBlockResourceType.INSTANCE, NamespaceID.from("demo:test_block2"))
            .model(Data.of(Path.of("resources/models/demo_block2.json")))
            .include(b -> b.file(Loc.of(NamespaceID.from("demo:texture3"), Loc.TEXTURES), Path.of("resources/textures/texture3.png")))
            .blockReplacement(BlockReplacement.NOTE_BLOCK)
            .build()
    );

    public static void init() {
        if (REGISTRY.isEmpty()) {
            add(TEST);
            add(TEST2);
        }
    }

    private static void add(Blocks block) {
        REGISTRY.put(block.getBlockResource().getNamespaceID(), block);
    }

}
