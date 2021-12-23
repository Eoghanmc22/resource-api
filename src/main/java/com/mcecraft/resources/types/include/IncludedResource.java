package com.mcecraft.resources.types.include;

import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class IncludedResource extends Resource {

	private final @NotNull String destPath;

	private final @Nullable String text;
	private final @Nullable Path path;

	public IncludedResource(@NotNull ResourceType<? extends Resource, ?> type, @NotNull NamespaceID namespaceID, @NotNull String destPath, @NotNull Path path) {
		super(type, namespaceID);

		this.destPath = destPath;
		this.path = path;
		this.text = null;
	}

	public IncludedResource(@NotNull ResourceType<? extends Resource, ?> type, @NotNull NamespaceID namespaceID, @NotNull String destPath, @NotNull String text) {
		super(type, namespaceID);

		this.destPath = destPath;
		this.text = text;
		this.path = null;
	}

	public @NotNull String getDestPath() {
		return destPath;
	}

	public @Nullable Path getPath() {
		return path;
	}

	public @Nullable String getText() {
		return text;
	}

}
