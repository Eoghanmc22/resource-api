package com.mcecraft.resources.types.item.persistence;

import com.mcecraft.resources.persistence.PersistenceStore;
import net.minestom.server.utils.NamespaceID;

import java.util.HashMap;
import java.util.Map;

public class ItemPersistenceStore implements PersistenceStore {
    Map<NamespaceID, ItemPersistenceData> data = new HashMap<>();
}
