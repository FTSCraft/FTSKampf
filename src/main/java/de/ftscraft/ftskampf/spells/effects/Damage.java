package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.Effect;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class Damage implements Effect {

    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    public Damage(Player player, Player target, double modifier) throws RaceDoNotExistException {
        diceManager.rollTargetDice(Dice.MAGIC, player, target, modifier);
    }
}