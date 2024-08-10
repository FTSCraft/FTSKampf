package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.Effect;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class HealLimitRange implements Effect {

    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    public HealLimitRange(Player player, Player target) throws RaceDoNotExistException {
        int range = 2;
        if(player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range).contains(target)) {
            diceManager.rollHealDice(Dice.MAGIC, player, target, 1.5);
        } else {
            diceManager.sendMessageInRange(Message.TAG + "§7Doch das Ziel ist zu weit entfernt!", player);
        }
    }
}