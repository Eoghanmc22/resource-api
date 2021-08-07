package com.mcecraft.resources.types.include;

import com.google.gson.JsonElement;
import com.mcecraft.resources.ResourceBuilder;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.Utils;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class IncludedResourceBuilder extends ResourceBuilder<IncludedResource> {

	private String destPath;

	private File file;
	private String text;

	protected IncludedResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<IncludedResource, ?> resourceType) {
		super(namespaceID, resourceType);
	}

	@Override
	protected @NotNull IncludedResource buildImpl() {
		if (destPath == null || (file == null && text == null)) {
			throw new NullPointerException("Incomplete builder!");
		}
		if (text != null) {
			return new IncludedResource(getResourceType(), destPath, text);
		} else {
			return new IncludedResource(getResourceType(), destPath, file);
		}
	}

	public @NotNull IncludedResourceBuilder texture(@NotNull Texture texture, @NotNull String location) {
		final int idx = location.lastIndexOf(File.separator);
		return file(texture.getPath() + location.substring(idx), location);
	}

	public @NotNull IncludedResourceBuilder file(@NotNull String destinationPath, @NotNull String currentPath) {
		return file(destinationPath, new File(currentPath));
	}

	public @NotNull IncludedResourceBuilder file(@NotNull String destinationPath, @NotNull File currentPath) {
		if (destPath != null || file != null) {
			throw new RuntimeException("Don't reuse builders!");
		}

		this.destPath = destinationPath;
		this.file = currentPath;

		return this;
	}

	public @NotNull IncludedResourceBuilder json(@NotNull String destinationPath, @NotNull JsonElement json) {
		return text(destinationPath, Utils.GSON.toJson(json));
	}

	public @NotNull IncludedResourceBuilder text(@NotNull String destinationPath, @NotNull String text) {
		this.destPath = destinationPath;
		this.text = text;

		return this;
	}

}
