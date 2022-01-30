package com.mcecraft.resources.types.block.real;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mcecraft.resources.*;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.types.block.real.persistence.RealBlockPersistenceData;
import com.mcecraft.resources.types.block.real.persistence.RealBlockPersistenceStore;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
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
    public @NotNull RealBlockResourceBuilder makeBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID) {
        return new RealBlockResourceBuilder(api, namespaceID, this);
    }

    @Override
    public @NotNull Generator<RealBlockResource, RealBlockPersistenceStore> createGenerator(@NotNull ResourceGenerator api, @NotNull PersistenceProvider<RealBlockPersistenceStore> dataProvider) {
        return new Generator<>() {

            final Int2ObjectMap<Set<RealBlockResource>> resources = new Int2ObjectOpenHashMap<>();
            final Short2ObjectMap<NamespaceID> claimedStates = new Short2ObjectOpenHashMap<>();

            {
                RealBlockPersistenceStore data = dataProvider.getData();
                if (data != null) {
                    for (Map.Entry<NamespaceID, RealBlockPersistenceData> entry : data.data.entrySet()) {
                        NamespaceID id = entry.getKey();
                        RealBlockPersistenceData blockData = entry.getValue();

                        NamespaceID maybeOld = claimedStates.put(blockData.stateId(), id);

                        if (maybeOld != null) {
                            throw new RuntimeException("Both " + maybeOld.asString() + " and " + id + " are claiming the same state id (" + blockData.stateId() + ")");
                        }
                    }
                }
            }

            @Override
            public @NotNull Collection<? extends Resource> dependencies(@NotNull ResourceGenerator api, @NotNull RealBlockResource resource, @NotNull PersistenceProvider<RealBlockPersistenceStore> dataProvider) {
                resources.computeIfAbsent(resource.getBlockReplacement().getBlockTypeId(), (key) -> new HashSet<>()).add(resource);

                Set<Resource> includes = new HashSet<>(resource.getIncludes());

                for (Pair<Data, BlockModelMeta> model : resource.getModels()) {
                    Data json = model.left();
                    BlockModelMeta meta = model.right();

                    includes.add(
                            api.create(IncludeType.INSTANCE, Utils.INTERNAL)
                                    .data(Loc.of(meta.model(), Loc.MODELS), json)
                                    .build(false)
                    );
                }

                return includes;
            }

            @Override
            public void generate(@NotNull ResourceGenerator api, @NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<RealBlockPersistenceStore> dataProvider) {
                RealBlockPersistenceStore data = dataProvider.getDataOr(RealBlockPersistenceStore::new);

                for (Int2ObjectMap.Entry<Set<RealBlockResource>> typeEntry : resources.int2ObjectEntrySet()) {
                    int blockTypeId = typeEntry.getIntKey();
                    Set<RealBlockResource> resourceSet = typeEntry.getValue();

                    Block blockType = Block.fromBlockId(blockTypeId);

                    if (blockType == null) {
                        throw new NullPointerException("No block type was found for block id " + blockTypeId);
                    }

                    Map<String, Set<BlockModelMeta>> state2model = new HashMap<>();

                    for (RealBlockResource resource : resourceSet) {
                        // get a state id for this resource
                        short blockStateId;

                        RealBlockPersistenceData blockData = data.data.get(resource.getNamespaceID());
                        if (blockData != null) {
                            blockStateId = blockData.stateId();
                        } else {
                            blockStateId = resource.getBlockReplacement().getNextBlock(claimedStates, resource.getNamespaceID());
                            claimedStates.put(blockStateId, resource.getNamespaceID());

                            if (resource.persist()) {
                                data.data.put(resource.getNamespaceID(), new RealBlockPersistenceData(blockType.namespace(), blockStateId));
                            }
                        }
                        resource.setStateId(blockStateId);

                        Block blockState = Block.fromStateId(blockStateId);
                        if (blockState == null) {
                            throw new NullPointerException("No block was found for state id " + blockStateId + " for " + resource.getNamespaceID());
                        }

                        // get the data the client needs to know about for this block
                        Set<BlockModelMeta> metaSet = new HashSet<>();
                        for (Pair<Data, BlockModelMeta> model : resource.getModels()) {
                            metaSet.add(model.right());
                        }

                        state2model.put(generateStateString(blockState.properties()), metaSet);
                    }


                    // generate the json files for the client
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

                    rp.include(Loc.of(blockType.namespace(), Loc.BLOCK_STATES), Json.json(blockStates));
                }
            }

            private @NotNull String generateStateString(@NotNull Map<String, String> propertiesMap) {
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
