package com.mcecraft.resources.testserver.items;

import com.mcecraft.resources.JsonProvider;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.Utils;
import com.mcecraft.resources.types.item.ItemResource;
import com.mcecraft.resources.types.item.ItemType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Items {

    private final ItemResource itemResource;

    public Items(ItemResource itemResource) {
        this.itemResource = itemResource;
    }

    public ItemResource getItemResource() {
        return itemResource;
    }

    public ItemStack create() {
        return itemResource.createItemStack();
    }



    public static final Map<NamespaceID, Items> REGISTRY = new HashMap<>();

    public static final Items TEST = new Items(ResourceApi.create(ItemType.INSTANCE, NamespaceID.from("demo:test_item"))
            .model(JsonProvider.of(Path.of("resources/models/test.json")))
            .include(b -> b.file(Utils.resourcePath(NamespaceID.from("demo:texture1"), Utils.TEXTURES), Path.of("resources/textures/texture1.png")))
            .material(Material.FIREWORK_ROCKET)
            .build()
    );

    public static void init() {
        if (REGISTRY.isEmpty()) {
            add(TEST);
        }
    }

    private static void add(Items item) {
        REGISTRY.put(item.getItemResource().getNamespaceID(), item);
    }

}
