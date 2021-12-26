package com.mcecraft.resources.types.block.real.replacement;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public interface BlockReplacement {

    BlockReplacement NOTE_BLOCK = SequentialBlockReplacement.of(Block.NOTE_BLOCK);

    static @NotNull BlockReplacement single(@NotNull Block block) {
        return new SingleBlockReplacement(block);
    }

    short getNextBlock();

}
