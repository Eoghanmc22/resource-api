package com.mcecraft.resources.types.item.persistence;

import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public record ItemPersistenceData(@NotNull Material material, int cmi) {

}
