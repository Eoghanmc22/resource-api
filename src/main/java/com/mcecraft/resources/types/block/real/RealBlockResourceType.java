package com.mcecraft.resources.types.block.real;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mcecraft.resources.*;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.types.block.real.persistence.RealBlockPersistenceStore;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import it.unimi.dsi.fastutil.Pair;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RealBlockResourceType implements ResourceType<RealBlockResource, RealBlockResourceBuilder, RealBlockPersistenceStore> {

    public static final RealBlockResourceType INSTANCE = new RealBlockResourceType();

    private RealBlockResourceType() {}

    @Override
    public @Nullable NamespaceID getPersistenceId() {
        return NamespaceID.from("resource_api:real_block");
    }

    @Override
    public @NotNull RealBlockResourceBuilder makeBuilder(@NotNull NamespaceID namespaceID) {
        return new RealBlockResourceBuilder(namespaceID, this);
    }

    @Override
    public @NotNull Generator<RealBlockResource, RealBlockPersistenceStore> createGenerator() {
        return new Generator<>() {

            final Map<Block, Set<RealBlockResource>> resources = new HashMap<>();

            @Override
            public @NotNull Collection<? extends Resource> dependencies(@NotNull RealBlockResource resource, @NotNull PersistenceProvider<RealBlockPersistenceStore> dataProvider) {
                short blockState = resource.getBlockReplacement().getNextBlock();
                Block block = Block.fromStateId(blockState);

                if (block == null) {
                    throw new RuntimeException("No block was found for state id " + blockState + " for " + resource.getNamespaceID());
                }

                Block blockType = Block.fromBlockId(block.id());

                if (blockType == null) {
                    throw new RuntimeException("No blockType was found for block " + block.namespace() + " for " + resource.getNamespaceID());
                }

                resource.setStateId(blockState);

                resources.computeIfAbsent(blockType, (key) -> new HashSet<>()).add(resource);

                Set<Resource> includes = new HashSet<>(resource.getIncludes());

                for (Pair<Data, BlockModelMeta> model : resource.getModels()) {
                    Data json = model.left();
                    BlockModelMeta meta = model.right();

                    includes.add(
                            ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)
                                    .data(Loc.of(meta.model(), Loc.MODELS), json)
                                    .build(false)
                    );
                }

                return includes;
            }

            @Override
            public void generate(@NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<RealBlockPersistenceStore> dataProvider) {
                for (Map.Entry<Block, Set<RealBlockResource>> typeEntry : resources.entrySet()) {
                    Block blockType = typeEntry.getKey();
                    Set<RealBlockResource> resourceSet = typeEntry.getValue();

                    Map<String, Set<BlockModelMeta>> state2model = new HashMap<>();

                    for (RealBlockResource resource : resourceSet) {
                        Set<BlockModelMeta> meta = new HashSet<>();

                        for (Pair<Data, BlockModelMeta> model : resource.getModels()) {
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
                            variants.add(state, Utils.toJsonTree(meta));
                        } else {
                            JsonArray array = new JsonArray();

                            for (BlockModelMeta meta : metaSet) {
                                array.add(Utils.toJsonTree(meta));
                            }

                            variants.add(state, array);
                        }
                    }
                    blockStates.add("variants", variants);

                    rp.include(Loc.of(blockType.namespace(), Loc.BLOCK_STATES), Json.of(blockStates));
                }
            }

            private String generateStateString(Map<String, String> propertiesMap) {
                StringBuilder sb = new StringBuilder();

                // Use a treemap so that the entries are in a consistent order
                for (Map.Entry<String, String> entry : new TreeMap<>(propertiesMap).entrySet()) {
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
