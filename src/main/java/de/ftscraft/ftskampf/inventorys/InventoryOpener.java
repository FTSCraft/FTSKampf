package de.ftscraft.ftskampf.inventorys;

import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class InventoryOpener {

    private static final HashMap<Player, String> prevInventory = new HashMap<>();
    private static final HashMap<Player, Player> playerTargetAlign = new HashMap<>();

    public static void openKampfInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Kampfsystem");

        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Nahkampfwürfel");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        inventory.setItem(11, item);

        item = new ItemStack(Material.PAPER);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Aktionswürfel");
        item.setItemMeta(itemMeta);
        inventory.setItem(13, item);

        item = new ItemStack(Material.BOW);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Fernkampfwürfel");
        item.setItemMeta(itemMeta);
        inventory.setItem(15, item);

        item = new ItemStack(Material.ENCHANTED_BOOK);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Magie");
        item.setItemMeta(itemMeta);
        inventory.setItem(29, item);

        item = new ItemStack(Material.ENDER_EYE);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Magie-Aktionswürfel");
        item.setItemMeta(itemMeta);
        inventory.setItem(31, item);

        item = new ItemStack(Material.LEATHER_BOOTS);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Agilitätswürfel");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        inventory.setItem(33, item);

        FTSKampf.kampfInventorys.add(inventory);
        prevInventory.put(player, "kampf");
        player.openInventory(inventory);
    }

    public static void openTargetInventory(Player player, Player target) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Kampfsystem auf " + target.getName());

        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Nahkampfwürfel");
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        inventory.setItem(11, item);

        item = new ItemStack(Material.BOW);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Fernkampfwürfel");
        item.setItemMeta(itemMeta);
        inventory.setItem(15, item);

        item = new ItemStack(Material.ENCHANTED_BOOK);
        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Magie");
        item.setItemMeta(itemMeta);
        inventory.setItem(29, item);

        FTSKampf.targetInventorys.put(inventory, target);
        playerTargetAlign.put(player, target);
        prevInventory.put(player, "target");
        player.openInventory(inventory);
    }

    public static void openPreviousInventory(Player player) {
        if(prevInventory.containsKey(player)) {
            String type = prevInventory.get(player);
            if(type.equals("kampf")) {
                openKampfInventory(player);
            } else {
                openTargetInventory(player, playerTargetAlign.get(player));
            }
        }
    }
}
