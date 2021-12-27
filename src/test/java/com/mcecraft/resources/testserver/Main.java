package com.mcecraft.resources.testserver;

import com.mcecraft.resources.DynamicResourcePack;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.testserver.blocks.Blocks;
import com.mcecraft.resources.testserver.items.Items;
import com.mcecraft.resources.utils.PackServer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.resourcepack.ResourcePack;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        MinecraftServer init = MinecraftServer.init();

        initContent();

        String packHash = generatePack();
        log.info("Pack hash is {}", packHash);

        initMinestom(packHash);

        init.start("0.0.0.0", 25565);
        PackServer.start("0.0.0.0", 8081);

        Runtime.getRuntime().addShutdownHook(new Thread(MinecraftServer::stopCleanly));
    }

    private static void initContent() {
        Items.init();

        for (NamespaceID item : Items.REGISTRY.keySet()) {
            System.out.println("Item " + item + " loaded");
        }

        Blocks.init();

        for (NamespaceID block : Blocks.REGISTRY.keySet()) {
            System.out.println("Block " + block + " loaded");
        }
    }

    private static @NotNull String generatePack() {
        DynamicResourcePack resourcePack = ResourceApi.generateResourcePack("A demo resource pack");
        PackServer.hostPack(resourcePack);
        return resourcePack.getHash();
    }

    private static void initMinestom(String packHash) {
        // setup instance
        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkGenerator(new ChunkGen());

        // add necessary events
        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, (event) -> event.setSpawningInstance(instance));

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, (event) -> {
            Player pl = event.getPlayer();

            pl.setResourcePack(ResourcePack.forced("http://localhost:8081" + PackServer.getPath(packHash), packHash));
            pl.getInventory().addItemStack(Items.TEST1.create());
            pl.getInventory().addItemStack(Items.TEST2.create());
            pl.teleport(new Pos(0, 45, 0));
            pl.setGameMode(GameMode.CREATIVE);
        });
    }
}
