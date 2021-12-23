package com.mcecraft.resources.types.block.real;

import com.mcecraft.resources.*;
import com.mcecraft.resources.types.include.IncludeType;
import com.mcecraft.resources.types.include.IncludedResource;
import com.mcecraft.resources.types.include.IncludedResourceBuilder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.UnaryOperator;

public class RealBlockResourceBuilder extends ResourceBuilder<RealBlockResource> {

    private BlockReplacement blockReplacement;
    private final List<IncludedResource> includes = new ArrayList<>();
    private final Set<Pair<JsonProvider, BlockModelMeta>> models = new HashSet<>();

    private boolean persist = true;

    protected RealBlockResourceBuilder(@NotNull NamespaceID namespaceID, @NotNull ResourceType<RealBlockResource, ?> resourceType) {
        super(resourceType, namespaceID);
    }

    @Override
    protected @NotNull RealBlockResource buildImpl() {
        if (blockReplacement == null || models.isEmpty()) {
            throw new NullPointerException("Incomplete builder!");
        }

        return new RealBlockResource(getResourceType(), getNamespaceID(), blockReplacement, includes, models, persist);
    }
    public RealBlockResourceBuilder blockReplacement(@NotNull BlockReplacement blockReplacement) {
        this.blockReplacement = blockReplacement;
        return this;
    }

    public RealBlockResourceBuilder include(@NotNull UnaryOperator<@NotNull IncludedResourceBuilder> resource) {
        includes.add(resource.apply(ResourceApi.create(IncludeType.INSTANCE, Utils.INTERNAL)).build(false));
        return this;
    }

    public RealBlockResourceBuilder model(@NotNull JsonProvider modelProvider) {
        models.add(new ObjectObjectImmutablePair<>(modelProvider, BlockModelMeta.from(Utils.prefixPath(getNamespaceID(), "block/"))));
        return this;
    }

    public RealBlockResourceBuilder model(@NotNull JsonProvider modelProvider, BlockModelMeta meta) {
        models.add(new ObjectObjectImmutablePair<>(modelProvider, meta));
        return this;
    }

    public RealBlockResourceBuilder persist(boolean persist) {
        this.persist = persist;
        return this;
    }
}
