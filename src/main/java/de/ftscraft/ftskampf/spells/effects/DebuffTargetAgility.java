package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;

public class DebuffTargetAgility extends ContinuousEffect {

    public DebuffTargetAgility(String target, String caster) {
        this(target, caster, 0.7, 3);
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