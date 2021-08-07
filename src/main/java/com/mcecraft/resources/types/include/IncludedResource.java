package com.mcecraft.resources.types.include;

import com.mcecraft.resources.Resource;
import com.mcecraft.resources.ResourceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class IncludedResource extends Resource {

	private final @NotNull String destPath;

	private final @Nullable String text;
	private final @Nullable File file;

	public IncludedResource(@NotNull ResourceType<? extends Resource, ?> type, @NotNull String destPath, @NotNull File file) {
		super(type);

		this.destPath = destPath;
		this.file = file;
		this.text = null;
	}

	public IncludedResource(@NotNull ResourceType<? extends Resource, ?> type, @NotNull String destPath, @NotNull String text) {
		super(type);

		this.destPath = destPath;
		this.text = text;
		this.file = null;
	}

	public @NotNull String getDestPath() {
		return destPath;
	}

	public @Nullable File getFile() {
		return file;
	}

	public @Nullable String getText() {
		return text;
	}

}
