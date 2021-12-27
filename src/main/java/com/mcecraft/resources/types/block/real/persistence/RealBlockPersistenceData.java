package com.mcecraft.resources.types.block.real.persistence;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// GSON doesnt seem to work with records
@SuppressWarnings("ClassCanBeRecord")
public final class RealBlockPersistenceData {
    private final @NotNull NamespaceID block;
    private final short stateId;

    public RealBlockPersistenceData(@NotNull NamespaceID block, short stateId) {
        this.block = block;
        this.stateId = stateId;
    }

    public @NotNull NamespaceID block() {
        return block;
    }

    public short stateId() {
        return stateId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RealBlockPersistenceData) obj;
        return Objects.equals(this.block, that.block) &&
                this.stateId == that.stateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, stateId);
    }

    @Override
    public String toString() {
        return "RealBlockPersistenceData[" +
                "block=" + block + ", " +
                "stateId=" + stateId + ']';
    }


}
