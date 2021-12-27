package com.mcecraft.resources.testserver;

import net.minestom.server.Bootstrap;

public class Start {

    public static void main(String[] args) {
        System.setProperty("minestom.extension.indevfolder.classes", "build/classes/java/main/");
        System.setProperty("minestom.extension.indevfolder.resources", "build/resources/main/");
        Bootstrap.bootstrap("com.mcecraft.resources.testserver.Main", args);
    }
}
