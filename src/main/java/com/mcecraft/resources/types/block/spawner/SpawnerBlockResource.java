package com.mcecraft.resources.types.block.spawner;

import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.types.block.BlockResource;
import com.mcecraft.resources.types.item.ItemResource;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTType;

public class SpawnerBlockResource extends Resource implements BlockResource {
    private final ItemResource item;

    public SpawnerBlockResource(@NotNull ResourceType<? extends Resource, ?, ?> resourceType, @NotNull NamespaceID namespaceID, @NotNull ItemResource item) {
        super(resourceType, namespaceID);

        this.item = item;
    }

    public @NotNull ItemResource getItem() {
        return item;
    }

    @Override
    public @NotNull Block createBlock() {
        ItemStack itemStack = item.createItemStack();

        NBTCompound spawnerNBT = NBT.Compound(compound -> {
            compound.setShort("RequiredPlayerRange", (short) 0);
            compound.setShort("MaxNearbyEntities", (short) 0);

            compound.set("SpawnData", NBT.Compound(spawnData -> {
                spawnData.set("entity", NBT.Compound(entity -> {
                    entity.setString("id", "armor_stand");
                    entity.set("Invisible", NBT.getTRUE());
                    entity.set("Marker", NBT.getTRUE());
                    entity.set("ArmorItems", NBT.List(NBTType.TAG_Compound, NBT.getEMPTY(), NBT.getEMPTY(), NBT.getEMPTY(), itemStack.toItemNBT()));
                }));
            }));
        });

        return Block.SPAWNER.withNbt(spawnerNBT);
    }
}
