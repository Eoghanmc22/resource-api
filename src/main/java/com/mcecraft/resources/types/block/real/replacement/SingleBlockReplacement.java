package com.mcecraft.resources.types.block.real.replacement;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public class SingleBlockReplacement implements BlockReplacement {

    private final Block block;
    private boolean used = false;

    public SingleBlockReplacement(@NotNull Block block) {
        this.block = block;
    }

    @Override
    public short getNextBlock(@NotNull Short2ObjectMap<NamespaceID> alreadyClaimed, @NotNull NamespaceID id) {
        if (used) {
            throw new RuntimeException("This SingleBlockReplacement has already been used");
        }

        used = true;

        short stateId = block.stateId();

        NamespaceID maybeClaimer = alreadyClaimed.get(stateId);

        if (maybeClaimer != null) {
            throw new RuntimeException("The block this SingleBlockReplacement (belonging to " + id + ") has already been claimed by a persistent block (" + maybeClaimer + ")");
        }

        return stateId;
    }

    @Override
    public int getBlockTypeId() {
        return block.id();
    }
}
