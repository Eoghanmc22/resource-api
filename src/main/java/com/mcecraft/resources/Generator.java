package com.mcecraft.resources;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface Generator {
	@Nullable Set<Resource> add(@NotNull Object resource);
	void generate(@NotNull GeneratedResourcePack rp);
}
