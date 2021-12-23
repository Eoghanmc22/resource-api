package com.mcecraft.resources.types.block.real;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludeType;
import it.unimi.dsi.fastutil.Pair;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RealBlockResourceType implements ResourceType<RealBlockResource, RealBlockResourceBuilder> {

    public static final RealBlockResourceType INSTANCE = new RealBlockResourceType();

    private RealBlockResourceType() {}

    @Override
    public @NotNull RealBlockResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
        return new RealBlockResourceBuilder(namespaceID, this);
    }

    @Override
    public @NotNull Generator<RealBlockResource> createGenerator() {
        return new Generator<>() {

            final Map<Block, Set<RealBlockResource>> resources = new HashMap<>();

            @Override
            public @NotNull Collection<? extends Resource> dependencies(@NotNull RealBlockResource resource) {
                resource.setStateId(resource.getBlockReplacement().getNextBlock());

                Block block = Block.fromBlockId(resource.getBlockReplacement().getBlockType().id());

                resources.computeIfAbsent(block, (key) -> new HashSet<>()).add(resource);

                Set<Resource> includes = new HashSet<>(resource.getIncludes());

                for (Pair<JsonProvider, BlockModelMeta> model : resource.getModels()) {
                    JsonProvider json = model.left();
                    BlockModelMeta meta = model.right();

                    includes.add(
                            ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)
                                    .json(
                                            Utils.resourcePath(meta.model(), Utils.MODELS),
                                            json.get()
                                    )
                                    .build(false)
                    );
                }

                return includes;
            }

            @Override
            public void generate(@NotNull GeneratedResourcePack rp) {
                for (Map.Entry<Block, Set<RealBlockResource>> typeEntry : resources.entrySet()) {
                    Block blockType = typeEntry.getKey();
                    Set<RealBlockResource> resourceSet = typeEntry.getValue();

                    Map<String, Set<BlockModelMeta>> state2model = new HashMap<>();

                    for (RealBlockResource resource : resourceSet) {
                        Set<BlockModelMeta> meta = new HashSet<>();

                        for (Pair<JsonProvider, BlockModelMeta> model : resource.getModels()) {
                            meta.add(model.right());
                        }

                        Block block = Block.fromStateId(resource.getStateId());

                        Objects.requireNonNull(block, "Something went quite wrong and the RealBlockResource " + resource.getNamespaceID() + " was assigned the invalid stateId " + resource.getStateId());

                        state2model.put(generateStateString(block.properties()), meta);
                    }

                    JsonObject blockStates = new JsonObject();

                    JsonObject variants = new JsonObject();
                    for (Map.Entry<String, Set<BlockModelMeta>> entry : state2model.entrySet()) {
                        String state = entry.getKey();
                        Set<BlockModelMeta> metaSet = entry.getValue();

                        if (metaSet.size() == 1) {
                            BlockModelMeta meta = metaSet.iterator().next();
                            variants.add(state, Utils.json(meta));
                        } else {
                            JsonArray array = new JsonArray();

                            for (BlockModelMeta meta : metaSet) {
                                array.add(Utils.json(meta));
                            }

                            variants.add(state, array);
                        }
                    }
                    blockStates.add("variants", variants);

                    rp.include(Utils.resourcePath(blockType.namespace(), Utils.BLOCK_STATES), Utils.GSON.toJson(blockStates));
                }
            }

            private String generateStateString(Map<String, String> propertiesMap) {
                StringBuilder sb = new StringBuilder();

                for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    sb.append(key);
                    sb.append("=");
                    sb.append(value);
                    sb.append(",");
                }

                return sb.toString();
            }
        };
    }
}
