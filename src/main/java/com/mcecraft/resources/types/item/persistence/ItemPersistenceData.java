package com.mcecraft.resources.types.item.persistence;

import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

// GSON doesnt seem to work with records
@SuppressWarnings("ClassCanBeRecord")
public final class ItemPersistenceData {
    private final @NotNull NamespaceID material;
    private final int cmi;

    public ItemPersistenceData(@NotNull NamespaceID material, int cmi) {
        this.material = material;
        this.cmi = cmi;
    }

    public @NotNull NamespaceID material() {
        return material;
    }

    public int cmi() {
        return cmi;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemPersistenceData) obj;
        return Objects.equals(this.material, that.material) &&
                this.cmi == that.cmi;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, cmi);
    }

    @Override
    public String toString() {
        return "ItemPersistenceData[" +
                "material=" + material + ", " +
                "cmi=" + cmi + ']';
    }


}
