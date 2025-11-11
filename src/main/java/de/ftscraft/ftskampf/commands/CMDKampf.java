package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.inventorys.InventoryOpener;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Message;
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

public class CMDKampf implements CommandExecutor {

    FTSKampf plugin = FTSKampf.getPlugin();
    Engine engine = plugin.getEngine();
    FileConfiguration config = plugin.getConfig();
    final HpManager hpManager = plugin.getHpManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        if(config.getBoolean("Permissions.UseDices.Required")) {
            if(!(player.hasPermission(config.getString("Permissions.UseDices.Name")))) {
                player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                return true;
            }
        }

        Ausweis ausweis = engine.getAusweis(player);
        if (ausweis == null || ausweis.getGender() == null || ausweis.getRace() == null) {
            player.sendMessage(Message.TAG + "§6Bitte lege dir zuerst einen Ausweis an (/ausweis). Für das Würfeln ist folgendes wichtig: §cName, Rasse, Geschlecht");
            return true;
        }

        if(args.length < 1) {
            InventoryOpener.openKampfInventory(player);
            return true;
        }



        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(Message.TAG + "§6Der Spieler §c" + args[0] + " §6konnte nicht gefunden werden!");
            return true;
        }
        if(target.equals(player)) {
            player.sendMessage(Message.TAG + "§6Du kannst dich nicht selbst angreifen!");
            return true;
        }

        int range = plugin.getConfig().getInt("DiceChatRange");
        boolean inRange = player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range).contains(target);

        if(!inRange) {
            player.sendMessage(Message.TAG + "§6Der Spieler §c" + args[0] + " §6ist zu weit entfernt!");
        }

        InventoryOpener.openTargetInventory(player, target);
        return true;
    }
}
