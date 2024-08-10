package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectDeliverer;

public class BuffAttackDebuffArmor extends ContinuousEffectDeliverer {

    public BuffAttackDebuffArmor(String target, String caster) {
        continuousEffects.add(new BuffAttack(target, caster, 1.2));
        continuousEffects.add(new DebuffTargetArmor(target, caster));
    }
}
