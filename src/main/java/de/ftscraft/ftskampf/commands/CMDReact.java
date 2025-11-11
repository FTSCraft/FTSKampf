package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.main.Logger;
import de.ftscraft.ftskampf.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDReact implements CommandExecutor {
    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        if (!diceManager.isAttacked(player)) {
            return true;
        }

        if (args.length < 1) {
            return true;
        }

        String reaction = args[0];
        switch (reaction) {
            case "dodge":
                diceManager.rollDodge(player);
                break;
            case "counter":
                diceManager.rollCounter(player);
                break;
            case "deny":
                diceManager.sendMessageInRange(Message.TAG + "§c§o" + player.getName() + " §r§chat den Angriff abgebrochen!", player);
                Logger.log(player, "canceld attack");
                break;
            case "accept":
                diceManager.rollAccept(player);
                break;
            default:
                return true;
        }
        diceManager.removeAttack(player);
        return true;
    }
}
