package com.mcecraft.resources.types.block.real.replacement;

import net.minestom.server.instance.block.Block;

public interface BlockReplacement {

    BlockReplacement NOTE_BLOCK = SequentialBlockReplacement.of(Block.NOTE_BLOCK);

    static BlockReplacement single(Block block) {
        return new SingleBlockReplacement(block);
    }

    short getNextBlock();

}
