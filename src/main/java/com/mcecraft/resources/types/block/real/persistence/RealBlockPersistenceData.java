package com.mcecraft.resources.types.block.real.persistence;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public record RealBlockPersistenceData(@NotNull Block block, short blockId) {

}
