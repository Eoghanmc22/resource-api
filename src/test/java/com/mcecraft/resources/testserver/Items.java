package com.mcecraft.resources.testserver;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.types.item.ItemResource;
import com.mcecraft.resources.types.item.ItemType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Items {

    public static final Map<NamespaceID, ItemResource> REGISTRY = new HashMap<>();

    public static final ItemResource TEST1 = ResourceApi.create(ItemType.INSTANCE, NamespaceID.from("demo:test_item"))
            .model(Data.of(Path.of("resources/models/test.json")))
            .include(b -> b.file(Loc.of(NamespaceID.from("demo:texture1"), Loc.TEXTURES), Path.of("resources/textures/texture1.png")))
            .material(Material.FIREWORK_ROCKET)
            .build();

    public static final ItemResource TEST2 = ResourceApi.create(ItemType.INSTANCE, NamespaceID.from("demo:test_item2"))
            .model(Data.of(Path.of("resources/models/test2.json")))
            .include(b -> b.file(Loc.of(NamespaceID.from("demo:texture4"), Loc.TEXTURES), Path.of("resources/textures/texture4.png")))
            .material(Material.FIREWORK_ROCKET)
            .build();


    public static void init() {
        if (REGISTRY.isEmpty()) {
            add(TEST1);
            add(TEST2);
        }
    }

    private static void add(@NotNull ItemResource item) {
        REGISTRY.put(item.getNamespaceID(), item);
    }

}
