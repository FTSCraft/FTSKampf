package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class DebuffTargetAgility extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public DebuffTargetAgility(String target, String caster) {
        this(target, caster, config.getDouble("SPELL_DEBUFFTARGETAGILITY_MODIFIER"),
                config.getInt("SPELL_DEBUFFTARGETAGILITY_DURABILITY"));
    }

    public DebuffTargetAgility(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.DEBUFF_TARGET_AGILITY, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        reduceDurability();
        if(!dice.equals(Dice.AGILITY))
            return value;
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "-";
    }

    @Override
    public String getEffectName() {
        return "Agilitätsdebuff";
    }
}