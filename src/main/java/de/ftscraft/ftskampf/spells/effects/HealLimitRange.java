package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.Effect;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class HealLimitRange implements Effect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();
    DiceManager diceManager = plugin.getDiceManager();

    public HealLimitRange(Player player, Player target) throws RaceDoNotExistException {
        int range = config.getInt("SPELL_HEALLIMITRANGE_RANGE");
        if(player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range).contains(target)) {
            diceManager.rollHealDice(Dice.MAGIC, player, target, config.getDouble("SPELL_HEALLIMITRANGE_MODIFIER"));
        } else {
            diceManager.sendMessageInRange(Message.TAG + "§7Doch das Ziel ist zu weit entfernt!", player);
        }
    }
}