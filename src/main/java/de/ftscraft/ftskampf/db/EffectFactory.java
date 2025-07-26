package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effectSpells.*;
import de.ftscraft.ftskampf.utils.exceptions.EffectNotImplementedException;

public class EffectFactory {
    public static EffectSpell getEffect(String spellEffectID) throws EffectNotImplementedException {
        return switch (spellEffectID) {
            case "1" -> new DamageSpell();
            case "2" -> new DamageKnockbackSpell();
            case "3" -> new DamagePenetrationSpell();
            case "4" -> new DamageAbsorptionSpell();
            case "5" -> new DamageOverTimeSpell();
            case "6" -> new HealSpell();
            case "7" -> new HealLimitRangeSpell();
            case "8" -> new HealRadiusSpell();
            case "9" -> new HealSelfDamageSpell();
            case "10" -> new BuffAttackSpell();
            case "11" -> new BuffAttackDebuffArmorSpell();
            case "12" -> new BuffUnarmedSpell();
            case "13" -> new BuffArmorSpell();
            case "14" -> new BuffAgilitySpell();
            case "15" -> new BuffAttackArmorRangeSpell();
            case "16" -> new ProtectAttackSpell();
            case "17" -> new DebuffTargetAttackSpell();
            case "18" -> new DebuffTargetArmorSpell();
            case "19" -> new DebuffTargetAgilitySpell();
            case "20" -> new StunSpell();
            case "21" -> new BlockMagicSpell();
            case "1E" -> new RaceDamageSpell("E");
            case "1Z" -> new RaceDamageSpell("Z");
            case "1O" -> new RaceDamageSpell("O");
            case "22E" -> new RaceHealSpell("E");
            case "22Z" -> new RaceHealSpell("Z");
            case "22O" -> new RaceHealSpell("O");
            case "23E" -> new RaceBuffAttackRadiusSpell("E");
            case "23Z" -> new RaceBuffAttackRadiusSpell("Z");
            case "23O" -> new RaceBuffAttackRadiusSpell("O");
            case "24E" -> new RaceBuffArmorRadiusSpell("E");
            case "24Z" -> new RaceBuffArmorRadiusSpell("Z");
            case "24O" -> new RaceBuffArmorRadiusSpell("O");
            case "25E" -> new RaceHealRadiusSpell("E");
            case "25Z" -> new RaceHealRadiusSpell("Z");
            case "25O" -> new RaceHealRadiusSpell("O");
            default -> throw new EffectNotImplementedException("Unknown spellEffect id:" + spellEffectID);
        };
    }
}
