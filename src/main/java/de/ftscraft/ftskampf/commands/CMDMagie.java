package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.db.DBManager;
import de.ftscraft.ftskampf.db.SpellManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CMDMagie implements CommandExecutor {

    FTSKampf plugin = FTSKampf.getPlugin();
    Engine engine = plugin.getEngine();
    FileConfiguration config = plugin.getConfig();
    SpellManager spellManager = plugin.getSpellManager();
    DBManager db = plugin.getDB();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        if (args.length < 1) {
            if (config.getBoolean("Permissions.ChooseSpells.Required")) {
                if (!(player.hasPermission(config.getString("Permissions.UseDices.Name")))) {
                    player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                    return true;
                }
            }

            Race race = plugin.getRace(player);

            if (race.getSkill(Dice.MAGIC) < 0) {
                player.sendMessage(Message.TAG + "§6Die Rasse §o" + race.getmName() + " §6verfügt nicht über die Fähigkeit §o" + Dice.MAGIC.getName() + "!");
                return true;
            }

            if (plugin.getConfig().getInt("MaxSpells") <= spellManager.playerGetNumberOfSpells(player.getUniqueId().toString())) {
                player.sendMessage(Message.TAG + "§6Du hast bereits alle Zauber ausgewählt!");
                return true;
            }

            Ausweis ausweis = engine.getAusweis(player);
            if (ausweis == null || ausweis.getGender() == null || ausweis.getRace() == null) {
                player.sendMessage(Message.TAG + "§6Bitte lege dir zuerst einen Ausweis an (/ausweis). Folgendes ist wichtig: §cName, Rasse, Geschlecht");
                return true;
            }

            Inventory inventory = Bukkit.createInventory(null, 9 * 5, "Zauber auswählen");
            int i = 0;
            HashMap<Integer, String> zidMapping = new HashMap<>();
            for (Spell spell : spellManager.getAllSpells()) {
                if (spell.raceMatches(ausweis.getRace()) && !spellManager.playerHasSpell(player.getUniqueId().toString(), spell)) {
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
            FTSKampf.spellChooseInventory.add(new MappedInventory(inventory, zidMapping));
            player.openInventory(inventory);
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            if (args.length < 2) {
                if (config.getBoolean("Permissions.ResetSpellsOther.Required")) {
                    if (!(player.hasPermission(config.getString("Permissions.ResetSpellsOther.Name")))) {
                        player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                        return true;
                    }
                }
                spellManager.playerResetSpells(player.getUniqueId().toString());
                player.sendMessage(Message.TAG + "§6Deine Zauber wurden zurückgesetzt!");
                return true;
            }


            //Reset Spells of others
            if (config.getBoolean("Permissions.ResetSpells.Required")) {
                if (!(player.hasPermission(config.getString("Permissions.ResetSpells.Name")))) {
                    player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                    return true;
                }
            }

            String target = args[1];
            String targetUuid;
            try {
                targetUuid = db.getUUID(target);
            } catch (Exception e) {
                player.sendMessage(Message.TAG + "§6Der Spieler §c" + target + " §6konnte nicht gefunden werden!");
                return true;
            }

            if (spellManager.playerResetSpells(targetUuid)) {
                player.sendMessage(Message.TAG + "§6Die Zauber von §c" + target + " §6wurden zurückgesetzt!");
            } else {
                player.sendMessage(Message.TAG + "§6Der Spieler §c" + target + " §6hatte keine Zauber gewählt");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("lookup")) {
            SpellCollection spellCollection = spellManager.getSpellCollection(player);
            if (spellCollection.getNumberOfSpells() < 1) {
                player.sendMessage(Message.TAG + "§7Du hast keine Zauber gelernt!");
                return true;
            }
            StringBuilder message = new StringBuilder(Message.TAG + "§7Du hast folgende Zauber gelernt: §c");
            List<Spell> spells = spellManager.getSpellCollection(player).getSpells();
            message.append(spells.get(0).getName());
            if (spells.size() > 1) {
                for (Spell spell : spells.subList(1, spells.size())) {
                    message.append(", ").append(spell.getName());
                }
            }
            player.sendMessage(message.toString());
        }
        return true;
    }
}