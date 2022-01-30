package com.mcecraft.resources.testserver;

import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.types.visual.ArmorStandVisualResource;
import com.mcecraft.resources.types.visual.ArmorStandVisualType;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Include;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Visuals {

    public static final Map<NamespaceID, ArmorStandVisualResource> REGISTRY = new HashMap<>();

    public static final ArmorStandVisualResource TEST_LARGE = ResourceApi.create(ArmorStandVisualType.INSTANCE, NamespaceID.from("demo:test_visual"))
            .model(
                    Data.path("resources/models/demo_block.json"),
                    Include.tex("demo:texture2", "resources/textures/texture2.png")
            )
            .build();

    public static final ArmorStandVisualResource TEST_SMALL = ResourceApi.create(ArmorStandVisualType.INSTANCE, NamespaceID.from("demo:test_visual_small"))
            .model(
                    Data.path("resources/models/demo_block.json"),
                    Include.tex("demo:texture2", "resources/textures/texture2.png")
            )
            .small(true)
            .build();


    public static void init() {
        if (REGISTRY.isEmpty()) {
            add(TEST_LARGE);
            add(TEST_SMALL);
        }
    }

    private static void add(@NotNull ArmorStandVisualResource visual) {
        REGISTRY.put(visual.getNamespaceID(), visual);
    }

}
