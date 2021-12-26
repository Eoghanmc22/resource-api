package com.mcecraft.resources;

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
public class ResourceApi {
    private static final Map<ResourceType<?, ?, ?>, ResourceContainer<?, ?, ?>> resourceTypes = new ConcurrentHashMap<>();
    private static final Set<Resource> resources = ConcurrentHashMap.newKeySet();
    private static final Path DATA_STORAGE = Path.of("resource_api/data.json");

    public static <R extends Resource, B extends ResourceBuilder<R>> @NotNull B create(@NotNull ResourceType<R, B, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return resourceType.makeBuilder(namespaceID);
    }

    public static <R extends Resource> @Nullable R lookup(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return (R) resourceTypes.get(resourceType).lookup(namespaceID);
    }

    static <R extends Resource> void register(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID, @NotNull R resource) {
        final ResourceContainer<R, ?, ?> resourceContainer = (ResourceContainer<R, ?, ?>) resourceTypes.computeIfAbsent(resourceType, ResourceContainer::new);
        resourceContainer.register(namespaceID, resource);
        resources.add(resource);
    }

    public static @NotNull DynamicResourcePack generateResourcePack(String packDescription) {
        DynamicResourcePack resourcePack = new DynamicResourcePack();
        GlobalPersistenceStore data = loadDataStores();

        resourcePack.include(Loc.any("pack.mcmeta"), Data.of("{\"pack\":{\"pack_format\":8,\"description\":\"" + packDescription + "\"}}"));

        Map<ResourceType<?, ?, ?>, Generator<?, ?>> generatorMap = new HashMap<>();

        Queue<Resource> queue = new LinkedList<>(resources);

        while (queue.peek() != null) {
            Resource resource = queue.poll();
            ResourceType<?, ?, ?> resourceType = resource.getResourceType();

            final Generator<?, ?> generator = generatorMap.computeIfAbsent(resourceType, ResourceType::createGenerator);

            PersistenceProvider<?> dataProvider = data.get(resourceType);

            queue.addAll(generator.__dependencies(resource, dataProvider));
        }
        generatorMap.forEach((resourceType, generator) -> generator.__generate(resourcePack, data.get(resourceType)));

        resourcePack.generate();

        return resourcePack;
    }

    private static @NotNull GlobalPersistenceStore loadDataStores() {
        try (Reader reader = Files.newBufferedReader(DATA_STORAGE)) {
            return Utils.GSON.fromJson(reader, GlobalPersistenceStore.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveDataStores(@NotNull GlobalPersistenceStore dataStores) {
        try (Writer writer = Files.newBufferedWriter(DATA_STORAGE)) {
            Utils.GSON.toJson(dataStores, GlobalPersistenceStore.class, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static class ResourceContainer<R extends Resource, B extends ResourceBuilder<R>, P extends PersistenceStore> {
        private final ResourceType<R, B, P> resourceType;
        private final Map<NamespaceID, R> registeredResources = new ConcurrentHashMap<>();

        private ResourceContainer(ResourceType<R, B, P> resourceType) {
            this.resourceType = resourceType;
        }

        public ResourceType<R, B, P> getResourceType() {
            return resourceType;
        }

        public R lookup(NamespaceID id) {
            return registeredResources.get(id);
        }

        public void register(NamespaceID id, R resource) {
            registeredResources.put(id, resource);
        }

        public Map<NamespaceID, R> getRegisteredResources() {
            return registeredResources;
        }

    }
}
