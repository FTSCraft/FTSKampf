package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDHeal implements CommandExecutor {

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

        if (config.getBoolean("Permissions.UseDices.Required")) {
            if (!(player.hasPermission(config.getString("Permissions.UseDices.Name")))) {
                player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                return true;
            }
        }

        Ausweis ausweis = engine.getAusweis(player);
        if (ausweis == null || ausweis.getGender() == null || ausweis.getRace() == null) {
            player.sendMessage(Message.TAG + "§6Bitte lege dir zuerst einen Ausweis an (/ausweis). Für das Würfeln ist folgendes wichtig: §cName, Rasse, Geschlecht");
            return true;
        }
        if (config.getBoolean("Permissions.Heal.Required")) {
            if (!(player.hasPermission(config.getString("Permissions.Heal.Name")))) {
                player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                return true;
            }
        }

        if (args.length < 2) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/heilen [Spieler] [HP zum heilen]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Message.TAG + "§6Der Spieler §c" + args[0] + " §6konnte nicht gefunden werden!");
            return true;
        }

        int range = plugin.getConfig().getInt("DiceChatRange");
        if (!player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range).contains(target)) {
            player.sendMessage(Message.TAG + "§6Der Spieler §c" + args[0] + " §6ist zu weit entfernt!");
            return true;
        }

        try {
            hpManager.healPlayer(target, Integer.parseInt(args[1]));
        } catch (NumberFormatException e) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/ftskampf heal [Spieler] [HP zum heilen]");
            return true;
        }
        String name;
        if (ausweis == null || ausweis.getFirstName() == null || ausweis.getLastName() == null) {
            name = player.getName();
        } else {
            name = ausweis.getFirstName() + " " + ausweis.getLastName();
        }

        String targetName;
        ausweis = engine.getAusweis(target);
        if (ausweis == null || ausweis.getFirstName() == null || ausweis.getLastName() == null) {
            targetName = args[0];
        } else {
            targetName = ausweis.getFirstName() + " " + ausweis.getLastName();
        }

        String message;

        if (hpManager.isMaxHealthReached(target)) {
            message = Message.TAG + "§c" + name + " §7hat §c" + targetName + " §7vollständig geheilt!";
        } else {
            message = Message.TAG + "§c" + name + " §7hat §c" + targetName + " §7um §c" + args[1] + " §7HP geheilt!";
        }

        String message2 = Message.TAG + "§c" + targetName + " §7hat nun §c" + hpManager.getHealth(target) + " §7HP!";

        for (Entity nearbyEntity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range)) {
            if (nearbyEntity instanceof Player) {
                nearbyEntity.sendMessage(message);
                nearbyEntity.sendMessage(message2);
            }
        }
        return true;
    }
}
