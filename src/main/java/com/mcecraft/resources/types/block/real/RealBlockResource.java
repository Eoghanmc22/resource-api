package com.mcecraft.resources.types.block.real;

import com.mcecraft.resources.types.block.real.replacement.BlockReplacement;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.types.block.BlockResource;
import com.mcecraft.resources.types.include.IncludedResource;
import it.unimi.dsi.fastutil.Pair;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RealBlockResource extends Resource implements BlockResource {
    private final @NotNull BlockReplacement blockReplacement;
    private final @NotNull List<IncludedResource> includes;
    private final @NotNull Set<Pair<Data, BlockModelMeta>> models;

    private final boolean persist;

    private short stateId;

    public RealBlockResource(@NotNull ResourceType<? extends Resource, ?> resourceType, @NotNull NamespaceID namespaceID, @NotNull BlockReplacement blockReplacement, @NotNull List<@NotNull IncludedResource> includes, @NotNull Set<Pair<Data, BlockModelMeta>> models, boolean persist) {
        super(resourceType, namespaceID);

        this.blockReplacement = blockReplacement;
        this.includes = includes;
        this.models = models;

        this.persist = persist;
    }

    public @NotNull BlockReplacement getBlockReplacement() {
        return blockReplacement;
    }

    public @NotNull List<@NotNull IncludedResource> getIncludes() {
        return includes;
    }

    public @NotNull Set<Pair<Data, BlockModelMeta>> getModels() {
        return models;
    }

    public boolean persist() {
        return persist;
    }

    public short getStateId() {
        return stateId;
    }

    void setStateId(short stateId) {
        this.stateId = stateId;
    }

    @Override
    public @NotNull Block createBlock() {
        if (stateId == 0) {
            throw new RuntimeException("The resource pack has not been generated yet");
        }

        Block block = Block.fromStateId(stateId);

        Objects.requireNonNull(block, "Something went quite wrong and the RealBlockResource " + getNamespaceID() + " was assigned the invalid stateId " + stateId);

        return block;
    }
}
