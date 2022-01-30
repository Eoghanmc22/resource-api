package com.mcecraft.resources;

import com.mcecraft.resources.utils.PackServer;
import net.kyori.adventure.text.Component;
import net.minestom.server.extensions.Extension;
import net.minestom.server.resourcepack.ResourcePack;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceApi extends Extension {

    //TODO config
    public static final String DEFAULT_ADDRESS = System.getProperty("resource_api.address", "0.0.0.0");
    public static final int DEFAULT_PORT = Integer.getInteger("resource_api.port", 8081);
    public static final boolean AUTO_START = System.getProperty("resource_api.start") == null;

    public static final ResourceGenerator GLOBAL_PACK = new ResourceGenerator();
    public static final PackServer GLOBAL_SERVER = new PackServer();

    private static @Nullable String packHash = null;

    @Override
    public void initialize() {

    }

    @Override
    public void postInitialize() {
        if (AUTO_START) {
            GLOBAL_SERVER.start();

            DynamicResourcePack rp = GLOBAL_PACK.generateResourcePack("A demo resource pack");
            GLOBAL_SERVER.hostPack(rp);
            packHash = rp.getHash();
        }
    }

    @Override
    public void terminate() {
        GLOBAL_SERVER.stop();
    }

    public static <R extends Resource, B extends ResourceBuilder<R>> @NotNull B create(@NotNull ResourceType<R, B, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return GLOBAL_PACK.create(resourceType, namespaceID);
    }

    public static <R extends Resource> @Nullable R lookup(@NotNull ResourceType<R, ?, ?> resourceType, @NotNull NamespaceID namespaceID) {
        return GLOBAL_PACK.lookup(resourceType, namespaceID);
    }

    public static @NotNull DynamicResourcePack generateResourcePack(@NotNull String packDescription) {
        return GLOBAL_PACK.generateResourcePack(packDescription);
    }

    public static @NotNull ResourcePack getResourcePack(@NotNull String publicIp, int publicPort) {
        String hash = packHash;
        Check.notNull(hash, "Resource pack not generated yet");
        String url = "http://" + publicIp + ":" + publicPort + GLOBAL_SERVER.getPath(hash);

        return ResourcePack.forced(url, hash, Component.text("This resource pack is needed for this server's content to function"));
    }
}
