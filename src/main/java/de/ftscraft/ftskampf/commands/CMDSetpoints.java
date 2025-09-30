package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftskampf.db.DBManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.NumberNegativeException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDSetpoints implements CommandExecutor {
    FTSKampf plugin = FTSKampf.getPlugin();
    DBManager dbManager = plugin.getDB();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        if(args.length < 2) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/setpoints [Spielername] [Punkte]/reset");
            return true;
        }

        if (args[1].equalsIgnoreCase("reset")) {
            try {
                dbManager.resetMaxSkillpoints(args[0]);
            } catch (Exception e) {
                player.sendMessage(Message.TAG + "§6Spieler §r§e" + args[0] + " §6konnte nicht gefunden werden!");
                return true;
            }
            player.sendMessage(Message.TAG + "§6Die maximalen Skillpunkte und Skills von §r§e" + args[0] + " §6wurden zurückgesetzt.");
            return true;
        }

        try {
            dbManager.setMaxSkillpoints(args[0], Integer.parseInt(args[1]));
        } catch (NumberNegativeException | NumberFormatException e) {
            player.sendMessage(Message.TAG + "§6Ungültige Zahl!");
            return true;
        } catch (Exception e) {
            player.sendMessage(Message.TAG + "§6Spieler §r§e" + args[0] + " §6konnte nicht gefunden werden!");
            return true;
        }
        player.sendMessage(Message.TAG + "§6Spieler§r§e " + args[0] + " §6hat nun §c" + args[1] + " §6maximale Skillpunkte.");
        player.sendMessage(Message.TAG + "§6Die Skills von §r§e" + args[0] + " §6wurden zurückgesetzt.");
        return true;
    }
}
