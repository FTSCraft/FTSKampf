package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;

public class DebuffTargetArmor extends ContinuousEffect {

    public DebuffTargetArmor(String target, String caster) {
        this(target, caster, 1.3, 3);
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
