package com.mcecraft.resources.types.block.real;

import net.minestom.server.instance.block.Block;

public interface BlockReplacement {

    BlockReplacement NOTE_BLOCK = new SequentialBlockReplacement(Block.NOTE_BLOCK);

    short getNextBlock();

    Block getBlockType();

}
