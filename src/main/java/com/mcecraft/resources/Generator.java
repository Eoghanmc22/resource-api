package com.mcecraft.resources;

import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.persistence.PersistenceStore;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface Generator<R extends Resource, P extends PersistenceStore> {
	default @NotNull Collection<? extends Resource> __dependencies(@NotNull ResourceGenerator api, @NotNull Object resource, @NotNull PersistenceProvider<?> persistenceData) { return dependencies(api, (R) resource, (PersistenceProvider<P>) persistenceData); }
	@NotNull Collection<? extends Resource> dependencies(@NotNull ResourceGenerator api, @NotNull R resource, @NotNull PersistenceProvider<P> persistenceData);

	default void __generate(@NotNull ResourceGenerator api, @NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<?> persistenceData) { generate(api, rp, (PersistenceProvider<P>) persistenceData); }
	void generate(@NotNull ResourceGenerator api, @NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<P> persistenceData);
}
