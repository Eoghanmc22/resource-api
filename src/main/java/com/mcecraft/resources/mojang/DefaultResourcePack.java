package com.mcecraft.resources.mojang;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Json;
import com.mcecraft.resources.utils.Loc;
import com.mcecraft.resources.utils.Utils;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;

public class DefaultResourcePack {

    public static final Path JAR_CACHE = Path.of("resource_api/jar_cache/");
    public static final Path DEFAULT_PACK = Path.of("resource_api/default_packs/", MinecraftServer.VERSION_NAME);

    private static final Logger logger = LoggerFactory.getLogger(DefaultResourcePack.class);
    private static final URL VERSION_MANIFEST;

    private static boolean available = false;

    static {
        try {
            VERSION_MANIFEST = new URL("https://launchermeta.mojang.com/mc/game/version_manifest_v2.json");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generate() {
        if (Files.exists(DEFAULT_PACK)) {
            available = true;
            logger.info("Found default resource pack (contents are not checked yet)");
        } else {
            try {
                logger.info("Default resource pack not found, generating it");

                logger.debug("Connecting to {}", VERSION_MANIFEST);

                URLConnection manifestConnection = VERSION_MANIFEST.openConnection();
                manifestConnection.connect();
                JsonArray versions;

                try (Reader r = new BufferedReader(new InputStreamReader(manifestConnection.getInputStream()))) {
                    versions = Utils.fromJsonReader(r).getAsJsonObject().get("versions").getAsJsonArray();
                }

                URL clientInfoURL = null;

                for (JsonElement version1 : versions) {
                    JsonObject version = version1.getAsJsonObject();

                    if (MinecraftServer.VERSION_NAME.equals(version.get("id").getAsString())) {
                        clientInfoURL = new URL(version.get("url").getAsString());

                        logger.info("Found client info for {}", MinecraftServer.VERSION_NAME);
                    }
                }

                if (clientInfoURL == null) {
                    logger.error("Could not find client info for {}", MinecraftServer.VERSION_NAME);
                    return;
                }

                logger.debug("Connecting to {}", clientInfoURL);

                URLConnection clientInfoConnection = clientInfoURL.openConnection();
                clientInfoConnection.connect();
                JsonObject clientData;

                try (Reader r = new BufferedReader(new InputStreamReader(clientInfoConnection.getInputStream()))) {
                    clientData = Utils.fromJsonReader(r).getAsJsonObject();
                }

                JsonObject client = clientData.get("downloads").getAsJsonObject().get("client").getAsJsonObject();
                String jarHash = client.get("sha1").getAsString();
                URL jarURL = new URL(client.get("url").getAsString());

                Path gamePath = JAR_CACHE.resolve(MinecraftServer.VERSION_NAME + ".jar");

                MessageDigest md = MessageDigest.getInstance("SHA-1");

                boolean download = true;

                if (Files.exists(gamePath)) {
                    try (OutputStream os = OutputStream.nullOutputStream(); InputStream is = new DigestInputStream(Files.newInputStream(gamePath), md)) {
                        is.transferTo(os);
                    }

                    String actualHash = Utils.hashDigestToString(md.digest());

                    if (actualHash.equals(jarHash)) {
                        logger.info("We dont need to download the jar because the cached one has a valid hash");
                        download = false;
                    } else {
                        logger.info("Cached jar has bad hash, downloading jar again");
                    }
                }

                if (download) {
                    logger.debug("Connecting to {}", jarURL);

                    URLConnection jarConnection = jarURL.openConnection();
                    jarConnection.connect();

                    Files.createDirectories(gamePath.getParent());

                    try (OutputStream os = Files.newOutputStream(gamePath); InputStream is = new DigestInputStream(jarConnection.getInputStream(), md)) {
                        is.transferTo(os);
                        os.flush();
                    }

                    String actualHash = Utils.hashDigestToString(md.digest());

                    if (!actualHash.equals(jarHash)) {
                        logger.error("hashes dont match");
                        return;
                    }
                }

                try (JarFile jar = new JarFile(gamePath.toFile())) {
                    jar.stream()
                            .filter(jarEntry -> jarEntry.getName().startsWith("assets/minecraft"))
                            .forEach(jarEntry -> {
                                try {
                                    Path path = DEFAULT_PACK.resolve(jarEntry.getName());

                                    Files.createDirectories(path.getParent());
                                    try (OutputStream os = Files.newOutputStream(path); InputStream is = jar.getInputStream(jarEntry)) {
                                        is.transferTo(os);
                                        os.flush();
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }

                available = true;
                logger.info("Successfully got default resource pack");
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final PathMatcher jsonMatcher = FileSystems.getDefault().getPathMatcher("glob:*.json");

    public static @NotNull Data get(@NotNull Loc loc) {
        if (!isAvailable()) {
            throw new RuntimeException("The default resource pack is not available likely due to an error earlier in the console");
        }

        Path path = DEFAULT_PACK.resolve(loc.getPath());

        if (!Files.exists(path)) {
            throw new RuntimeException("No assist at `" + loc.getPath() + " was found in the default resource pack");
        }

        if (jsonMatcher.matches(path)) {
            return Json.of(path);
        } else {
            return Data.of(path);
        }
    }

    public static boolean isAvailable() {
        return available;
    }
}
