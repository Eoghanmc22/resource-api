package com.mcecraft.resources.types.block.real.replacement;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public interface BlockReplacement {

    BlockReplacement NOTE_BLOCK = SequentialBlockReplacement.of(Block.NOTE_BLOCK);

    static @NotNull BlockReplacement single(@NotNull Block block) {
        return new SingleBlockReplacement(block);
    }

    short getNextBlock(@NotNull Short2ObjectMap<NamespaceID> alreadyClaimed, @NotNull NamespaceID id);

    int getBlockTypeId();
}
