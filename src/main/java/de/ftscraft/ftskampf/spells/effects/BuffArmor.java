package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;

public class BuffArmor extends ContinuousEffect {

    public BuffArmor(String target, String caster) {
        this(target, caster, 0.7, 3);
    }

    public BuffArmor(String target, String caster, double modifier) {
        this(target, caster, modifier, 3);
    }

    public BuffArmor(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_ARMOR, modifier, durability);
    }

    @Override
    public int modifyDefendValue(int value, Dice dice) {
        super.reduceDurability();
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "+";
    }

    @Override
    public String getEffectName() {
        return "Verteidigungsbuff";
    }
}