package com.mcecraft.resources;

import com.mcecraft.resources.mojang.DefaultResourcePack;
import com.mcecraft.resources.persistence.GlobalPersistenceStore;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.persistence.PersistenceStore;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class ResourceGenerator {
    private static final Path DATA_STORAGE = Path.of("resource_api/data.json");

    static {
        DefaultResourcePack.generate();
    }

    private final Map<ResourceType<?, ?, ?>, ResourceContainer<?, ?, ?>> resourceTypes = new ConcurrentHashMap<>();
    private final Set<Resource> resources = ConcurrentHashMap.newKeySet();

    public <R extends Resource, B extends ResourceBuilder<R>> @NotNull B create(@NotNull ResourceType<R, B, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return resourceType.makeBuilder(this, namespaceID);
    }

    public <R extends Resource> @Nullable R lookup(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return (R) resourceTypes.get(resourceType).lookup(namespaceID);
    }

    <R extends Resource> void register(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID, @NotNull R resource) {
        final ResourceContainer<R, ?, ?> resourceContainer = (ResourceContainer<R, ?, ?>) resourceTypes.computeIfAbsent(resourceType, ResourceContainer::new);
        resourceContainer.register(namespaceID, resource);
        resources.add(resource);
    }

    public @NotNull DynamicResourcePack generateResourcePack(@NotNull String packDescription) {
        DynamicResourcePack resourcePack = new DynamicResourcePack();
        GlobalPersistenceStore data = loadDataStores();

        resourcePack.include(Loc.any("pack.mcmeta"), Data.str("{\"pack\":{\"pack_format\":8,\"description\":\"" + packDescription + "\"}}"));

        Map<ResourceType<?, ?, ?>, Generator<?, ?>> generatorMap = new HashMap<>();

        Queue<Resource> queue = new LinkedList<>(resources);

        while (queue.peek() != null) {
            Resource resource = queue.poll();
            ResourceType<?, ?, ?> resourceType = resource.getResourceType();

            PersistenceProvider<?> dataProvider = data.get(resourceType);

            final Generator<?, ?> generator = generatorMap.computeIfAbsent(resourceType, resourceType1 -> resourceType1.__createGenerator(this, dataProvider));

            queue.addAll(generator.__dependencies(this, resource, dataProvider));
        }
        generatorMap.forEach((resourceType, generator) -> generator.__generate(this, resourcePack, data.get(resourceType)));

        resourcePack.generate();

        saveDataStores(data);

        return resourcePack;
    }

    private static @NotNull GlobalPersistenceStore loadDataStores() {
        if (Files.exists(DATA_STORAGE)) {
            try (Reader reader = Files.newBufferedReader(DATA_STORAGE)) {
                return Utils.GSON.fromJson(reader, GlobalPersistenceStore.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new GlobalPersistenceStore();
        }
    }

    private static void saveDataStores(@NotNull GlobalPersistenceStore dataStores) {
        try {
            Files.createDirectories(DATA_STORAGE.getParent());

            try (Writer writer = Files.newBufferedWriter(DATA_STORAGE)) {
                Utils.GSON.toJson(dataStores, GlobalPersistenceStore.class, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static class ResourceContainer<R extends Resource, B extends ResourceBuilder<R>, P extends PersistenceStore> {
        private final ResourceType<R, B, P> resourceType;
        private final Map<NamespaceID, R> registeredResources = new ConcurrentHashMap<>();

        private ResourceContainer(@NotNull ResourceType<R, B, P> resourceType) {
            this.resourceType = resourceType;
        }

        public @NotNull ResourceType<R, B, P> getResourceType() {
            return resourceType;
        }

        public @Nullable R lookup(@NotNull NamespaceID id) {
            return registeredResources.get(id);
        }

        public void register(@NotNull NamespaceID id, @NotNull R resource) {
            registeredResources.put(id, resource);
        }

        public @NotNull Map<NamespaceID, R> getRegisteredResources() {
            return registeredResources;
        }

    }
}
