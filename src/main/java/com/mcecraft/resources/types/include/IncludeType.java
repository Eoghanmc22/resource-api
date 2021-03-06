package com.mcecraft.resources.types.include;

import com.mcecraft.resources.*;
import com.mcecraft.resources.persistence.NullPersistenceStore;
import com.mcecraft.resources.persistence.PersistenceProvider;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Loc;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class IncludeType implements ResourceType<IncludedResource, IncludedResourceBuilder, NullPersistenceStore> {

	public static final IncludeType INSTANCE = new IncludeType();

	private IncludeType() {}

    @Override
	public @NotNull IncludedResourceBuilder makeBuilder(@NotNull ResourceGenerator api, @NotNull NamespaceID namespaceID) {
		return new IncludedResourceBuilder(api, namespaceID, this);
	}

	@Override
	public @NotNull Generator<IncludedResource, NullPersistenceStore> createGenerator(@NotNull ResourceGenerator api, @NotNull PersistenceProvider<NullPersistenceStore> _store) {
		return new Generator<>() {

			private final Set<IncludedResource> resources = new HashSet<>();

			@Override
			public @NotNull Collection<? extends Resource> dependencies(@NotNull ResourceGenerator api, @NotNull IncludedResource resource, @NotNull PersistenceProvider<NullPersistenceStore> _store) {
				resources.add(resource);
				return Collections.emptyList();
			}

			@Override
			public void generate(@NotNull ResourceGenerator api, @NotNull DynamicResourcePack rp, @NotNull PersistenceProvider<NullPersistenceStore> _store) {
				for (IncludedResource resource : resources) {
					for (Map.Entry<Loc, Data> entry : resource.getResources().entrySet()) {
						Loc loc = entry.getKey();
						Data data = entry.getValue();

						rp.include(loc, data);
					}
				}
			}
		};
	}

}
