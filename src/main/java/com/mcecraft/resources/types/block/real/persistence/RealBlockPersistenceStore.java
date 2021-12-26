package com.mcecraft.resources.types.block.real.persistence;

import com.mcecraft.resources.persistence.PersistenceStore;
import net.minestom.server.utils.NamespaceID;

import java.util.HashMap;
import java.util.Map;

public class RealBlockPersistenceStore implements PersistenceStore {
    Map<NamespaceID, RealBlockPersistenceData> data = new HashMap<>();
}
