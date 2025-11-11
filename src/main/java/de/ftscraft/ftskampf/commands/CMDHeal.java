package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.main.Logger;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.NumberNegativeException;
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
    FileConfiguration config = plugin.getConfig();
    final HpManager hpManager = plugin.getHpManager();
    final int RANGE = 20;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
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

        try {
            hpManager.healPlayer(target, Integer.parseInt(args[1]));
        } catch (NumberNegativeException e) {
            player.sendMessage(Message.TAG + "§7Du musst mehr als 0 Punkte angeben!");
            return  true;
        } catch (Exception e) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/ftskampf heal [Spieler] [HP zum heilen]");
            return true;
        }


        Logger.log(player, "healed " + target.getName() + ", " + Integer.parseInt(args[1]) + " by command /heal");


        String message;

        if (hpManager.isMaxHealthReached(target)) {
            message = Message.TAG + "§7Ein Admin hat §c" + target.getName() + " §7vollständig geheilt!";
        } else {
            message = Message.TAG + "§7Ein Admin hat §c" + target.getName() + " §7um §c" + args[1] + " §7HP geheilt!";
        }

        String message2 = Message.TAG + "§c" + target.getName() + " §7hat nun §c" + hpManager.getHealth(target) + " §7HP!";

        for (Entity nearbyEntity : target.getLocation().getWorld().getNearbyEntities(target.getLocation(), RANGE, RANGE, RANGE)) {
            if (nearbyEntity instanceof Player) {
                nearbyEntity.sendMessage(message);
                nearbyEntity.sendMessage(message2);
            }
        }
        return true;
    }
}
