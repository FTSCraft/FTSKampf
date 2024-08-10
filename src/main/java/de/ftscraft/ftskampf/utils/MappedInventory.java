package de.ftscraft.ftskampf.utils;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MappedInventory {
    private Inventory inventory;
    private HashMap<Integer, String> zidMapping;

    public MappedInventory(Inventory inventory, HashMap<Integer, String> zidMapping) {
        this.inventory = inventory;
        this.zidMapping = zidMapping;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HashMap<Integer, String> getZidMapping() {
        return zidMapping;
    }

    public boolean equals(Inventory inventory) {
        return this.inventory.equals(inventory);
    }
}
