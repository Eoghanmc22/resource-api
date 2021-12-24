package com.mcecraft.resources;

import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class ResourceApi {
    private static final Map<ResourceType<?, ?>, ResourceContainer<?, ?>> resourceTypes = new ConcurrentHashMap<>();
    private static final Set<Resource> resources = ConcurrentHashMap.newKeySet();

    public static <R extends Resource, B extends ResourceBuilder<R>> @NotNull B create(@NotNull ResourceType<R, B> resourceType, @NotNull NamespaceID namespaceID) {
        return resourceType.makeBuilder(namespaceID);
    }

    public static <R extends Resource> @Nullable R lookup(@NotNull ResourceType<R, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return (R) resourceTypes.get(resourceType).lookup(namespaceID);
    }

    static <R extends Resource> void register(@NotNull ResourceType<R, ?> resourceType, @NotNull NamespaceID namespaceID, @NotNull R resource) {
        final ResourceContainer<R, ?> resourceContainer = (ResourceContainer<R, ?>) resourceTypes.computeIfAbsent(resourceType, ResourceContainer::new);
        resourceContainer.register(namespaceID, resource);
        resources.add(resource);
    }

    public static @NotNull ResourcePack generateResourcePack(String packDescription) {
        ResourcePack resourcePack = new ResourcePack();

        resourcePack.include(Loc.any("pack.mcmeta"), Data.of("{\"pack\":{\"pack_format\":8,\"description\":\"" + packDescription + "\"}}"));

        Map<ResourceType<?, ?>, Generator<?>> generatorMap = new HashMap<>();

        Queue<Resource> queue = new LinkedList<>(resources);

        while (queue.peek() != null) {
            Resource resource = queue.poll();

            final Generator<?> generator = generatorMap.computeIfAbsent(resource.getResourceType(), ResourceType::createGenerator);

            queue.addAll(generator.__dependencies(resource));
        }
        generatorMap.forEach((resourceType, generator) -> generator.generate(resourcePack));

        resourcePack.generate();

        return resourcePack;
    }

    private static class ResourceContainer<R extends Resource, B extends ResourceBuilder<R>> {
        private final ResourceType<R, B> resourceType;
        private final Map<NamespaceID, R> registeredResources = new ConcurrentHashMap<>();

        private ResourceContainer(ResourceType<R, B> resourceType) {
            this.resourceType = resourceType;
        }

        public ResourceType<R, B> getResourceType() {
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
