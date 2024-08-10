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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDHurt implements CommandExecutor {
    final FTSKampf plugin = FTSKampf.getPlugin();
    final HpManager hpManager = plugin.getHpManager();
    Engine engine = plugin.getEngine();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        if (plugin.getConfig().getBoolean("Testmode")) {
            player.sendMessage(Message.TAG + "§6Befehl nicht verfügbar!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/hurt [Spieler] [HP zum heilen]]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Message.TAG + "§6Der Spieler §c" + args[0] + " §6konnte nicht gefunden werden!");
        }

        try {
            hpManager.hurtPlayer(target, Integer.parseInt(args[1]));
        } catch (NumberFormatException e) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/hurt [Spieler] [HP zum heilen]]");
            return true;
        }

        String name;
        Ausweis ausweis = engine.getAusweis(target);
        if (ausweis == null || ausweis.getFirstName() == null || ausweis.getLastName() == null) {
            name = args[0];
        } else {
            name = ausweis.getFirstName() + " " + ausweis.getLastName();
        }

        if (hpManager.getHealth(target) == 0) {
            player.sendMessage(Message.TAG + "§c" + name + " §6ist kampfunfähig!");
        }

        return true;
    }
}
