package com.mcecraft.resources.testserver;

import com.mcecraft.resources.GeneratedResourcePack;
import com.mcecraft.resources.ResourceApi;
import com.mcecraft.resources.testserver.blocks.Blocks;
import com.mcecraft.resources.testserver.items.Items;
import com.mcecraft.resources.types.block.spawner.SpawnerBlockResource;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.resourcepack.ResourcePack;
import net.minestom.server.utils.NamespaceID;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        MinecraftServer init = MinecraftServer.init();

        Items.init();

        for (NamespaceID item : Items.REGISTRY.keySet()) {
            System.out.println("Item " + item + " loaded");
        }

        Blocks.init();

        for (NamespaceID block : Blocks.REGISTRY.keySet()) {
            System.out.println("Block " + block + " loaded");
        }

        GeneratedResourcePack resourcePack = ResourceApi.generateResourcePack("A demo resource pack");

        try {
            PackServer.run(resourcePack);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkGenerator(new ChunkGen());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, (event) -> event.setSpawningInstance(instance));

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, (event) -> {
            event.getPlayer().setResourcePack(ResourcePack.forced("http://localhost:8081/pack.zip", resourcePack.getHash()));
            event.getPlayer().getInventory().addItemStack(Items.TEST.create());
            event.getPlayer().getInventory().addItemStack(((SpawnerBlockResource)Blocks.TEST.getBlockResource()).getItem().createItemStack());
            event.getPlayer().teleport(new Pos(0, 45, 0));
        });

        init.start("0.0.0.0", 25565);

        Runtime.getRuntime().addShutdownHook(new Thread(MinecraftServer::stopCleanly));

        System.out.println(resourcePack.getHash());
    }
}