package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;

public class BuffUnarmed extends ContinuousEffect {

    public BuffUnarmed(String target, String caster) {
        this(target, caster, 1.8, 3);
    }

    public BuffUnarmed(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_UNARMED, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        if(!isUnarmed)
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
        return "Unbewaffneter Nahkampf Buff";
    }
}
