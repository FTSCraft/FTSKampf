package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;

public class ProtectAttack extends ContinuousEffect {

    public ProtectAttack(String target, String caster) {
        this(target, caster, -1, 3);
    }

    public ProtectAttack(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.PROTECT_ATTACK, modifier, durability);
    }

    @Override
    public boolean isProtected() {
        reduceDurability();
        return true;
    }

    @Override
    public String getBuffSign() {
        return "+";
    }

    @Override
    public String getEffectName() {
        return "Immunität";
    }
}