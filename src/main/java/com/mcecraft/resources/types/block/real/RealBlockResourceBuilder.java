package com.mcecraft.resources.types.block.real;

import com.mcecraft.resources.*;
import com.mcecraft.resources.types.block.real.replacement.BlockReplacement;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.include.IncludedResource;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import com.mcecraft.resources.utils.Data;
import com.mcecraft.resources.utils.Utils;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.UnaryOperator;

public class RealBlockResourceBuilder extends ResourceBuilder<RealBlockResource> {

    private BlockReplacement blockReplacement;
    private final List<IncludedResource> includes = new ArrayList<>();
    private final Set<Pair<Data, BlockModelMeta>> models = new HashSet<>();

    private boolean persist = true;

    protected RealBlockResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<RealBlockResource, ?, ?> resourceType) {
        super(resourceType, namespaceID);
    }

    @Override
    protected @NotNull RealBlockResource buildImpl() {
        if (blockReplacement == null || models.isEmpty()) {
            throw new NullPointerException("Incomplete builder!");
        }

        return new RealBlockResource(getResourceType(), getNamespaceID(), blockReplacement, includes, models, persist);
    }
    public @NotNull RealBlockResourceBuilder blockReplacement(@NotNull BlockReplacement blockReplacement) {
        this.blockReplacement = blockReplacement;
        return this;
    }

    public @NotNull RealBlockResourceBuilder include(@NotNull UnaryOperator<@NotNull IncludedResourceBuilder> resource) {
        includes.add(resource.apply(ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)).build(false));
        return this;
    }

    public @NotNull RealBlockResourceBuilder model(@NotNull Data model) {
        models.add(new ObjectObjectImmutablePair<>(model, BlockModelMeta.from(Utils.prefixPath(getNamespaceID(), "block/"))));
        return this;
    }

    public @NotNull RealBlockResourceBuilder model(@NotNull Data model, BlockModelMeta meta) {
        models.add(new ObjectObjectImmutablePair<>(model, meta));
        return this;
    }

    public @NotNull RealBlockResourceBuilder persist(boolean persist) {
        this.persist = persist;
        return this;
    }
}
