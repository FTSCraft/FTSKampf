package de.ftscraft.ftskampf.utils;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MappedInventory {
    private final Inventory inventory;
    private final HashMap<Integer, String> idMapping;
    private final MappedInventoryType type;

    public MappedInventory(Inventory inventory, HashMap<Integer, String> idMapping, MappedInventoryType type) {
        this.inventory = inventory;
        this.idMapping = idMapping;
        this.type = type;
    }

    public HashMap<Integer, String> getIdMapping() {
        return idMapping;
    }

    public MappedInventoryType getType() {
        return type;
    }

    public boolean equals(Inventory inventory) {
        return this.inventory.equals(inventory);
    }

    public enum MappedInventoryType {
        CLASS_INVENTORY,
        SPELL_INVENTORY,
        CAST_INVENTORY;
    }
}
