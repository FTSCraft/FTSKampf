package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.spells.effects.*;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;

public class ContinuousEffectFactory {

    public static ContinuousEffect createEffect(String[] effectData) {
        String effectId = effectData[0];
        String target = effectData[1];
        String caster = effectData[2];
        double modifier = Double.parseDouble(effectData[3]);
        int durability = Integer.parseInt(effectData[4]);

        return createEffect(effectId, target, caster, modifier, durability);
    }

    public static ContinuousEffect createEffect(String effectId, String target, String caster, double modifier, int durability) {
        switch (effectId) {
            case "001":  // BUFF_AGILITY
                return new BuffAgility(target, caster, modifier, durability);
            case "002":  // BUFF_ARMOR
                return new BuffArmor(target, caster, modifier, durability);
            case "003":  // BUFF_ATTACK
                return new BuffAttack(target, caster, modifier, durability);
            case "004":  // BUFF_UNARMED
                return new BuffUnarmed(target, caster, modifier, durability);
            case "005":  // DAMAGE_OVER_TIME
                return new DamageOverTime(target, caster, modifier, durability);
            case "006":  // DEBUFF_TARGET_AGILITY
                return new DebuffTargetAgility(target, caster, modifier, durability);
            case "007":  // DEBUFF_TARGET_ARMOR
                return new DebuffTargetArmor(target, caster, modifier, durability);
            case "008":  // DEBUFF_TARGET_ATTACK
                return new DebuffTargetAttack(target, caster, modifier, durability);
            case "009":  // PROTECT_ATTACK
                return new ProtectAttack(target, caster, modifier, durability);
            default:
                throw new IllegalArgumentException("Unknown effect id: " + effectId);
        }
    }
}
