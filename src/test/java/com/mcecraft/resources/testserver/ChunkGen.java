package com.mcecraft.resources.testserver;

import com.mcecraft.resources.testserver.blocks.Blocks;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChunkGen implements ChunkGenerator {
    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 42; y++) {
                    batch.setBlock(x, y, z, Block.STONE);
                }
            }
        }
        if (chunkX == 0 && chunkZ == 0) {
            batch.setBlock(0, 44, 0, Blocks.TEST.create());
        }
        if (chunkX == 0 && chunkZ == 0) {
            batch.setBlock(1, 44, 0, Blocks.TEST2.create());
            batch.setBlock(2, 44, 0, Blocks.TEST2.create());
            batch.setBlock(3, 44, 0, Blocks.TEST2.create());
            batch.setBlock(4, 44, 0, Blocks.TEST2.create());
        }
    }

    @Override
    public @Nullable List<ChunkPopulator> getPopulators() {
        return null;
    }
}
