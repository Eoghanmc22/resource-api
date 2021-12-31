package com.mcecraft.resources.testserver;

import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.types.visual.ArmorStandVisualResource;
import com.mcecraft.resources.types.visual.ArmorStandVisualType;
import com.mcecraft.resources.utils.Data;
import net.minestom.server.entity.Entity;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public record Visuals(@NotNull ArmorStandVisualResource armorStandVisualResource) {

    public @NotNull Entity create() {
        return armorStandVisualResource.createEntity();
    }


    public static final Map<NamespaceID, Visuals> REGISTRY = new HashMap<>();

    public static final Visuals TEST_LARGE = new Visuals(ResourceApi.create(ArmorStandVisualType.INSTANCE, NamespaceID.from("demo:test_visual"))
            .model(Data.of(Path.of("resources/models/demo_block.json")))
            // already included
            //.include(b -> b.file(Loc.of(NamespaceID.from("demo:texture2"), Loc.TEXTURES), Path.of("resources/textures/texture2.png")))
            .build()
    );

    public static final Visuals TEST_SMALL = new Visuals(ResourceApi.create(ArmorStandVisualType.INSTANCE, NamespaceID.from("demo:test_visual_small"))
            .model(Data.of(Path.of("resources/models/demo_block.json")))
            // already included
            //.include(b -> b.file(Loc.of(NamespaceID.from("demo:texture2"), Loc.TEXTURES), Path.of("resources/textures/texture2.png")))
            .small(true)
            .build()
    );

    public static void init() {
        if (REGISTRY.isEmpty()) {
            add(TEST_LARGE);
            add(TEST_SMALL);
        }
    }

    private static void add(@NotNull Visuals visual) {
        REGISTRY.put(visual.armorStandVisualResource().getNamespaceID(), visual);
    }

}
