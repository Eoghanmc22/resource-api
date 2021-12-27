package com.mcecraft.resources.types.block.real.replacement;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SequentialBlockReplacement implements BlockReplacement {

    private final short endId;
    private final Block blockType;
    private short currentId = 1;

    private SequentialBlockReplacement(@NotNull Block blockType) {
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
    public short getNextBlock(@NotNull Short2ObjectMap<NamespaceID> alreadyClaimed, @NotNull NamespaceID id) {
        while (alreadyClaimed.containsKey(currentId)) {
            currentId++;
        }
        checkId();
        return currentId;
    }

    @Override
    public int getBlockTypeId() {
        return blockType.id();
    }

    private void checkId() {
        if (currentId > endId) {
            throw new RuntimeException("Too many block ids have been taken for " + blockType.namespace());
        }
    }


    private static final Map<Block, SequentialBlockReplacement> blockReplacements = new HashMap<>();

    public static @NotNull SequentialBlockReplacement of(@NotNull Block block) {
        Block blockType = Block.fromBlockId(block.id());

        if (blockType == null) {
            throw new RuntimeException("No block type could be found for " + block.namespace());
        }

        return blockReplacements.computeIfAbsent(blockType, SequentialBlockReplacement::new);
    }
}
