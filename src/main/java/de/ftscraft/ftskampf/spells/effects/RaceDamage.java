package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.RaceEffect;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class RaceDamage extends RaceEffect {
    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    public RaceDamage(Player player, Player target, String raceName) throws RaceDoNotExistException {
        if(checkTargetRace(raceName, target)) {
            diceManager.rollTargetDice(Dice.MAGIC, player, target, 1.7);
        } else {
            player.sendMessage(STR."\{Message.TAG}§cDieser Zauber ist nur auf die Rasse §6\{raceName} §canwendbar!");
        }

    }
}
