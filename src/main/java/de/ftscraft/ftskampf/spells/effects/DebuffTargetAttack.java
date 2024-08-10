package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;

public class DebuffTargetAttack extends ContinuousEffect {

    public DebuffTargetAttack(String target, String caster) {
        this(target, caster, 0.7, 3);
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