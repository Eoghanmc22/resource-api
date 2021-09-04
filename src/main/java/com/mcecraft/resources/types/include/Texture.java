package com.mcecraft.resources.types.include;

import org.jetbrains.annotations.NotNull;

public enum Texture {
	BLOCK("assets/minecraft/textures/blocks/"),
	ITEM("assets/minecraft/textures/item/"),
	FONT("assets/minecraft/textures/font/")
	;

	private final @NotNull String path;

	Texture(@NotNull String path) {

		this.path = path;
	}

	public @NotNull String getPath() {
		return path;
	}
}
