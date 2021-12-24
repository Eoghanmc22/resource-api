package com.mcecraft.resources;

import com.mcecraft.resources.mojang.DefaultResourcePack;
import net.minestom.server.extensions.Extension;

public class ResourceExtension extends Extension {

    @Override
    public void initialize() {
        DefaultResourcePack.generate();
    }

    @Override
    public void terminate() {

    }
}
