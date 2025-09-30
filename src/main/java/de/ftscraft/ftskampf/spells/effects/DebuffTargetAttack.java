package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class DebuffTargetAttack extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public DebuffTargetAttack(String target, String caster) {
        this(target, caster, config.getDouble("SPELL_DEBUFFTARGETATTACK_MODIFIER"), config.getInt("SPELL_DEBUFFTARGETATTACK_DURABILITY"));
    }

    public DebuffTargetAttack(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.DEBUFF_TARGET_ATTACK, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        reduceDurability();
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "-";
    }

    @Override
    public String getEffectName() {
        return "Angriffsdebuff";
    }
}