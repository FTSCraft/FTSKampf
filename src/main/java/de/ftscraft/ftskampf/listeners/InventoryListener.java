package de.ftscraft.ftskampf.listeners;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.SpellManager;
import de.ftscraft.ftskampf.inventorys.MagicInventory;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.utils.*;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryListener implements Listener {
    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();
    SpellManager spellManager = plugin.getSpellManager();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (FTSKampf.kampfInventorys.contains(inventory)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            try {
                switch (slot) {
                    case 11:
                        inventory.close();
                        diceManager.rollDice(Dice.MELEE, player);
                        break;
                    case 13:
                        inventory.close();
                        diceManager.rollActionDice(Dice.ACTION, player);
                        break;
                    case 15:
                        inventory.close();
                        diceManager.rollDice(Dice.DISTANCE, player);
                        break;
                    case 29:
                        inventory.close();
                        MagicInventory inv = new MagicInventory(player);
                        inv.show();
                        break;
                    case 33:
                        inventory.close();
                        diceManager.rollDice(Dice.AGILITY, player);
                }
            } catch (RaceDoNotExistException e) {
                player.sendMessage(Message.TAG + "§6Deine Rasse wurde nicht gefunden!");
                inventory.close();
            }
        }

        if (FTSKampf.targetInventorys.containsKey(inventory)) {
            Player target = FTSKampf.targetInventorys.get(inventory);
            Player player = (Player) event.getWhoClicked();
            if (!target.isOnline()) {
                player.sendMessage(Message.TAG + "§6Der Spieler §c" + target.getName() + " §6ist nicht mehr online!");
                return;
            }
            event.setCancelled(true);
            int slot = event.getRawSlot();
            try {
                switch (slot) {
                    case 11:
                        inventory.close();
                        diceManager.rollTargetDice(Dice.MELEE, player, target);
                        break;
                    case 15:
                        inventory.close();
                        diceManager.rollTargetDice(Dice.DISTANCE, player, target);
                        break;
                    case 29:
                        inventory.close();
                        MagicInventory inv = new MagicInventory(player);
                        inv.show(target);
                }
            } catch (RaceDoNotExistException e) {
                player.sendMessage(Message.TAG + "§6Deine Rasse wurde nicht gefunden!");
                inventory.close();
            }
        }

        if (isSpellClassChooseInventory(inventory)) {
            Player player = (Player) event.getWhoClicked();
            HashMap<Integer, String> idMapping = getSpellClassChooseInventory(inventory).getIdMapping();
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (!idMapping.containsKey(slot)) {
                return;
            }
            inventory.close();
            String id = idMapping.get(slot);
            Inventory newInventory = Bukkit.createInventory(null, 9 * 5, "Zauber auswählen");
            int i = 0;
            HashMap<Integer, String> newMapping = new HashMap<>();
            for (Spell spell : spellManager.getClassById(id).getSpells()) {
                if (!spellManager.playerHasSpell(spell)) {
                    ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(spell.getName());
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(itemMeta);
                    newInventory.setItem(i, item);
                    newMapping.put(i++, spell.getId());
                }

            }
            FTSKampf.spellChooseInventory.add(new MappedInventory(newInventory, newMapping, MappedInventory.MappedInventoryType.SPELL_INVENTORY));
            player.openInventory(newInventory);
        }

        if (isSpellChooseInventory(inventory)) {
            Player player = (Player) event.getWhoClicked();
            HashMap<Integer, String> idMapping = getSpellChooseInventory(inventory).getIdMapping();
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (!idMapping.containsKey(slot)) {
                return;
            }
            inventory.close();
            String id = idMapping.get(slot);
            player.sendMessage(Message.TAG + "§7Der Zauber §c" + spellManager.getSpellById(id).getName() + "§7 wurde gelernt.");
            spellManager.playerAddSpell(player.getUniqueId().toString(), id);
        }

        if (isSpellCastInventory(inventory)) {
            Player player = (Player) event.getWhoClicked();
            HashMap<Integer, String> zidMapping = getSpellCastInventory(inventory).getIdMapping();
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (!zidMapping.containsKey(slot)) {
                return;
            }
            inventory.close();

            Engine engine = plugin.getEngine();
            Ausweis ausweis = engine.getAusweis(player);
            Ausweis.Gender gender = ausweis.getGender();
            Race race = plugin.getRaceOrDefault(player);

            String article = "Der";
            String raceName = race.getmName();
            if (gender.equals(Ausweis.Gender.FEMALE)) {
                article = "Die";
                raceName = race.getfName();
            }

            String zid = zidMapping.get(slot);
            Spell spell = spellManager.getSpellById(zid);

            StringBuilder message;
            PlainResult result = new PlainResult(-1, 100, true);
            if (!spell.skipsPreDicing()) {
                result = diceManager.rollMagicPreDice(player);
                message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(player) + " §7setzt §a" + spell.getName() + " §7ein und würfelt: §e");
                if (result.isSuccess()) {
                    message.append("§2").append(result.getResult()).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(Dice.MAGIC.getName()).append("]");
                } else {
                    message.append("§c").append(result.getResult()).append(" §7hätte aber §c").append(result.getSkill()).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(Dice.MAGIC.getName()).append("]");
                    return;
                }
            } else {
                message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(player) + " §7setzt §a" + spell.getName() + " §7ein!");
            }
            diceManager.sendMessageInRange(message, player);

            if (result.isSuccess()) {
                spell.doEffect(player, player, result.getResult());
            }
        }

        if (isSpellTargetInventory(inventory)) {
            Player player = (Player) event.getWhoClicked();
            HashMap<Integer, String> zidMapping = getSpellTargetInventory(inventory).getIdMapping();
            event.setCancelled(true);

            Player target = FTSKampf.spellTargetInventory.get(getSpellTargetInventory(inventory));
            if (!target.isOnline()) {
                player.sendMessage(Message.TAG + "§6Das Ziel ist nicht mehr online!");
                inventory.close();
                return;
            }
            int slot = event.getRawSlot();
            if (!zidMapping.containsKey(slot)) {
                return;
            }
            inventory.close();

            Engine engine = plugin.getEngine();
            Ausweis ausweis = engine.getAusweis(player);
            Ausweis.Gender gender = ausweis.getGender();
            Race race = plugin.getRaceOrDefault(player);

            String article = "Der";
            String raceName = race.getmName();
            if (gender.equals(Ausweis.Gender.FEMALE)) {
                article = "Die";
                raceName = race.getfName();
            }

            Race targetRace = plugin.getRaceOrDefault(target);

            String articleTarget = "den";
            String raceNameTarget = targetRace.getmName();
            String targetName = target.getName();
            if (engine.hasAusweis(target)) {
                Ausweis targetAusweis = engine.getAusweis(target);
                if (gender.equals(Ausweis.Gender.FEMALE)) {
                    articleTarget = "die";
                    raceNameTarget = race.getfName();
                }
                targetName = targetAusweis.getFirstName() + " " + targetAusweis.getLastName();
            }

            String id = zidMapping.get(slot);
            Spell spell = spellManager.getSpellById(id);

            StringBuilder message;
            PlainResult result = new PlainResult(-1, 100, true);
            if (!spell.skipsPreDicing()) {
                result = diceManager.rollMagicPreDice(player);
                message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(player) + " §7wirkt §a" + spell.getName() + " §7auf " + articleTarget + " §o" + raceNameTarget + " §e" + targetName + " §7und würfelt: §e");
                if (result.isSuccess()) {
                    message.append("§2").append(result.getResult()).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(Dice.MAGIC.getName()).append("]");
                } else {
                    message.append("§c").append(result.getResult()).append(" §7hätte aber §c").append(result.getSkill()).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(Dice.MAGIC.getName()).append("]");
                    return;
                }
            } else {
                message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(player) + " §7wirkt §a" + spell.getName() + " §7auf " + articleTarget + " §o" + raceNameTarget + " §e" + targetName);
            }
            diceManager.sendMessageInRange(message, player);

            if (result.isSuccess()) {
                spell.doEffect(player, target, result.getResult());
            }
        }
    }

    private boolean isSpellChooseInventory(Inventory inventory) {
        for (MappedInventory mappedInventory : FTSKampf.spellChooseInventory) {
            if (mappedInventory.equals(inventory) & mappedInventory.getType().equals(MappedInventory.MappedInventoryType.SPELL_INVENTORY))
                return true;
        }
        return false;
    }

    private boolean isSpellClassChooseInventory(Inventory inventory) {
        for (MappedInventory mappedInventory : FTSKampf.spellChooseInventory) {
            if (mappedInventory.equals(inventory) & mappedInventory.getType().equals(MappedInventory.MappedInventoryType.CLASS_INVENTORY))
                return true;
        }
        return false;
    }

    private boolean isSpellCastInventory(Inventory inventory) {
        for (MappedInventory mappedInventory : FTSKampf.spellCastInventory) {
            if (mappedInventory.equals(inventory)) return true;
        }
        return false;
    }

    private boolean isSpellTargetInventory(Inventory inventory) {
        List<MappedInventory> mappedInventories = new ArrayList<>();
        mappedInventories.addAll(FTSKampf.spellTargetInventory.keySet());
        for (MappedInventory mappedInventory : mappedInventories) {
            if (mappedInventory.equals(inventory)) return true;
        }
        return false;
    }

    private MappedInventory getSpellChooseInventory(Inventory inventory) {
        for (MappedInventory mappedInventory : FTSKampf.spellChooseInventory) {
            if (mappedInventory.equals(inventory) & mappedInventory.getType().equals(MappedInventory.MappedInventoryType.SPELL_INVENTORY))
                return mappedInventory;
        }
        return null;
    }

    private MappedInventory getSpellClassChooseInventory(Inventory inventory) {
        for (MappedInventory mappedInventory : FTSKampf.spellChooseInventory) {
            if (mappedInventory.equals(inventory) & mappedInventory.getType().equals(MappedInventory.MappedInventoryType.CLASS_INVENTORY))
                return mappedInventory;
        }
        return null;
    }

    private MappedInventory getSpellCastInventory(Inventory inventory) {
        for (MappedInventory mappedInventory : FTSKampf.spellCastInventory) {
            if (mappedInventory.equals(inventory)) return mappedInventory;
        }
        return null;
    }

    private MappedInventory getSpellTargetInventory(Inventory inventory) {
        List<MappedInventory> mappedInventories = new ArrayList<>();
        mappedInventories.addAll(FTSKampf.spellTargetInventory.keySet());
        for (MappedInventory mappedInventory : mappedInventories) {
            if (mappedInventory.equals(inventory)) return mappedInventory;
        }
        return null;
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (FTSKampf.kampfInventorys.contains(event.getInventory())) event.setCancelled(true);
        if (FTSKampf.targetInventorys.containsKey(event.getInventory())) event.setCancelled(true);
        if (isSpellChooseInventory(event.getInventory())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        FTSKampf.kampfInventorys.remove(event.getInventory());
        FTSKampf.targetInventorys.remove(event.getInventory());
        FTSKampf.spellChooseInventory.remove(getSpellChooseInventory(event.getInventory()));
    }
}
