package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;

public class BuffAttack extends ContinuousEffect {

    public BuffAttack(String target, String caster) {
        this(target, caster, 1.5, 3);
    }

    public BuffAttack(String target, String caster, double modifier) {
        this(target, caster, modifier, 3);
    }

    public BuffAttack(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_ATTACK, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        if(dice.equals(Dice.AGILITY))
            return value;
        reduceDurability();
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "+";
    }

    @Override
    public String getEffectName() {
        return "Angriffsbuff";
    }
}