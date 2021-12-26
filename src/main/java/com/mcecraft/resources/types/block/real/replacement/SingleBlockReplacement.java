package com.mcecraft.resources.types.block.real.replacement;

import net.minestom.server.instance.block.Block;

public class SingleBlockReplacement implements BlockReplacement {

    private final Block block;
    private boolean used = false;

    public SingleBlockReplacement(Block block) {
        this.block = block;
    }

    @Override
    public short getNextBlock() {
        if (used) {
            throw new RuntimeException("This SingleBlockReplacement has already been used");
        }

        used = true;

        return block.stateId();
    }
}
