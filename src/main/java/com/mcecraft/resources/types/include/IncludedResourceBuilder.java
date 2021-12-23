package com.mcecraft.resources.types.include;

import com.google.gson.JsonElement;
import com.mcecraft.resources.ResourceBuilder;
import com.mcecraft.resources.ResourceType;
import com.mcecraft.resources.Utils;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class IncludedResourceBuilder extends ResourceBuilder<IncludedResource> {

	private String destPath;

	private Path file;
	private String text;

	protected IncludedResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<IncludedResource, ?> resourceType) {
		super(resourceType, namespaceID);
	}

	@Override
	protected @NotNull IncludedResource buildImpl() {
		if (destPath == null || (file == null && text == null)) {
			throw new NullPointerException("Incomplete builder!");
		}
		if (text != null) {
			return new IncludedResource(getResourceType(), getNamespaceID(), destPath, text);
		} else {
			return new IncludedResource(getResourceType(), getNamespaceID(), destPath, file);
		}
	}

	public @NotNull IncludedResourceBuilder file(@NotNull String destinationPath, @NotNull Path currentPath) {
		if (destPath != null) {
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
		if (destPath != null) {
			throw new RuntimeException("Don't reuse builders!");
		}

		this.destPath = destinationPath;
		this.text = text;

		return this;
	}

}
