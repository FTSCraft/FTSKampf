package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftskampf.db.DBManager;
import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.db.SpellManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDftskampfdb implements CommandExecutor {


    FTSKampf plugin = FTSKampf.getPlugin();
    FileConfiguration config = plugin.getConfig();
    DBManager dbManager = plugin.getDB();
    EffectManager effectManager = plugin.getEffectManager();
    HpManager hpManager = plugin.getHpManager();
    SpellManager spellManager = plugin.getSpellManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        if (config.getBoolean("Permissions.DBAdmin.Required")) {
            if (!(player.hasPermission(config.getString("Permissions.DBAdmin.Name")))) {
                player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                return true;
            }
        }

        if(args.length < 1) {
            player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/ftskampfdb [magie/effekte/skills/hp]");
            return true;
        }

        String type = args[0];
        switch (type) {
            case "magie":
                spellManager.reset();
                break;
            case "effekte":
                effectManager.reset();
                break;
            case "skills":
                dbManager.reset();
                break;
            case "hp":
                hpManager.reset();
                break;
            default:
                player.sendMessage(Message.TAG + "§6Bitte verwende den Befehl so: §c/ftskampfdb [magie/effekte/skills/hp]");
        }


        return true;
    }
}
