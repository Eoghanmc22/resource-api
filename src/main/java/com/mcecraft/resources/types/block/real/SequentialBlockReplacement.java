package com.mcecraft.resources.types.block.real;

import net.minestom.server.instance.block.Block;

public class SequentialBlockReplacement implements BlockReplacement {

    private final short endId;
    private final Block blockType;
    private short currentId = 1;

    public SequentialBlockReplacement(Block blockType) {
        this.blockType = blockType;

        short possibleEndId = -1;
        for (Block block : blockType.possibleStates()) {
            short stateId = block.stateId();
            if (stateId > possibleEndId) {
                possibleEndId = stateId;
            }
        }
        endId = possibleEndId;

        // this is `1 + stateId` because java cant add shorts for some reason
        currentId += blockType.stateId();

        checkId();
    }

    @Override
    public short getNextBlock() {
        checkId();
        return currentId++;
    }

    @Override
    public Block getBlockType() {
        return blockType;
    }

    private void checkId() {
        if (currentId > endId) {
            throw new RuntimeException("Too many block ids have been taken for " + blockType.namespace());
        }
    }
}
