package com.mcecraft.resources.types.block;

import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

public interface BlockResource {

    @NotNull NamespaceID getNamespaceID();
    @NotNull Block createBlock();
}
