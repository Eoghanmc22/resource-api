package com.mcecraft.resources.types.visual;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

class DisplayEntity extends LivingEntity {

    public DisplayEntity(@NotNull ItemStack displayedItem, boolean isSmall) {
        super(EntityType.ARMOR_STAND);

        this.hasPhysics = false;
        this.canPickupItem = false;
        this.invulnerable = true;

        setHelmet(displayedItem);
        setBoundingBox(new BoundingBox(this, 0, 0, 0));

        ArmorStandMeta meta = (ArmorStandMeta) getEntityMeta();
        meta.setOnFire(true);
        meta.setInvisible(true);
        meta.setMarker(true);
        meta.setSmall(isSmall);
    }

    @Override
    public void tick(long time) {
        if (instance == null || isRemoved())
            return;

        scheduler().processTick();
    }
}
