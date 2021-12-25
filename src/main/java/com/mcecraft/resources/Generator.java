package com.mcecraft.resources;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Generator<R> {
	default @NotNull Collection<? extends Resource> __dependencies(@NotNull Object resource) { return dependencies((R) resource); }

	@NotNull Collection<? extends Resource> dependencies(@NotNull R resource);
	void generate(@NotNull DynamicResourcePack rp);
}
