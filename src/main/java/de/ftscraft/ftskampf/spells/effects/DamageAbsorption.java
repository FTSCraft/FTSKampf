package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.Effect;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DamageAbsorption implements Effect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();
    private static final FileConfiguration config = plugin.getConfig();

    public DamageAbsorption(Player player, Player target) throws RaceDoNotExistException {
        diceManager.rollTargetDice(Dice.MAGIC, player, target, config.getDouble("SPELL_DAMAGEABSORPTION_MODIFIER"),
                config.getDouble("SPELL_DAMAGEABSORPTION_ABSORPTIONRATE"));
    }
}