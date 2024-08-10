package de.ftscraft.ftskampf.inventorys;

import de.ftscraft.ftskampf.db.SpellManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.utils.MappedInventory;
import de.ftscraft.ftskampf.utils.SpellCollection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MagicInventory {

    FTSKampf plugin = FTSKampf.getPlugin();
    SpellManager spellManager = plugin.getSpellManager();
    Player player;
    SpellCollection spellCollection;

    public MagicInventory(Player player) {
        this.player = player;
        this.spellCollection = spellManager.getSpellCollection(player);
    }

    public void show() {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Zauber auswählen");
        int i = 0;
        HashMap<Integer, String> zidMapping = new HashMap<>();
        for(Spell spell : spellCollection.getSpells()) {
            if(spell.isSelfApplicable()) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(spell.getName());
                List<String> lore = new ArrayList<>();
                lore.add(spell.getDescription());
                itemMeta.setLore(lore);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(itemMeta);
                inventory.setItem(i, item);
                zidMapping.put(i++, spell.getZid());
            }
        }
        FTSKampf.spellCastInventory.add(new MappedInventory(inventory, zidMapping));
        player.openInventory(inventory);
    }

    public void show(Player target) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Zauber auswählen, Ziel: " + target.getName());
        int i = 0;
        HashMap<Integer, String> zidMapping = new HashMap<>();
        for(Spell spell : spellCollection.getSpells()) {
            if(spell.isTargetApplicable()) {
                ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(spell.getName());
                List<String> lore = new ArrayList<>();
                lore.add(spell.getDescription());
                itemMeta.setLore(lore);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                item.setItemMeta(itemMeta);
                inventory.setItem(i, item);
                zidMapping.put(i++, spell.getZid());
            }
        }
        FTSKampf.spellTargetInventory.put(new MappedInventory(inventory, zidMapping), target);
        player.openInventory(inventory);
    }
}
