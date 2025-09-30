package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class DebuffTargetArmor extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public DebuffTargetArmor(String target, String caster) {
        this(target, caster, config.getDouble("SPELL_DEBUFFTARGETARMOR_MODIFIER"), config.getInt("SPELL_DEBUFFTARGETARMOR_DURABILITY"));
    }

    public DebuffTargetArmor(String target, String caster, double modifier) {
        this(target, caster, modifier, config.getInt("SPELL_DEBUFFTARGETARMOR_DURABILITY"));
    }

    public DebuffTargetArmor(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.DEBUFF_TARGET_ARMOR, modifier, durability);
    }

    @Override
    public int modifyDefendValue(int value, Dice dice) {
        reduceDurability();
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "-";
    }

    @Override
    public String getEffectName() {
        return "Verteidigungsdebuff";
    }
}
