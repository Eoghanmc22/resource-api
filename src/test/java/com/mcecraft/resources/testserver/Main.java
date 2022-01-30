package com.mcecraft.resources.testserver;

import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.types.visual.ArmorStandVisualType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.setProperty("minestom.extension.indevfolder.classes", "build/classes/java/main/");
        System.setProperty("minestom.extension.indevfolder.resources", "build/resources/main/");

        initContent();

        initMinestom();

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

        Visuals.init();

        for (NamespaceID visual : Visuals.REGISTRY.keySet()) {
            System.out.println("Visual " + visual + " loaded");
        }
    }

    private static void initMinestom() {
        MinecraftServer server = MinecraftServer.init();

        // setup instance
        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkGenerator(new ChunkGen());

        // add necessary events
        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, (event) -> event.setSpawningInstance(instance));

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, (event) -> {
            Player pl = event.getPlayer();

            pl.setResourcePack(ResourceApi.getResourcePack("localhost", ResourceApi.DEFAULT_PORT));
            pl.getInventory().addItemStack(Items.TEST1.createItemStack());
            pl.getInventory().addItemStack(Items.TEST2.createItemStack());
            pl.teleport(new Pos(0, 45, 0));
            pl.setGameMode(GameMode.CREATIVE);
        });

        server.start("0.0.0.0", 25565);

        Entity entity = Visuals.TEST_LARGE.createEntity();
        entity.setInstance(instance, new Pos(0.5, 41, 0.5).add(ArmorStandVisualType.SPAWN_OFFSET));

        Entity entity2 = Visuals.TEST_SMALL.createEntity();
        entity2.setInstance(instance, new Pos(1.5, 41, 0.5).add(ArmorStandVisualType.SPAWN_OFFSET_SMALL));
    }
}
