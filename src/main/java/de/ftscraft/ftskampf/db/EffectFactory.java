package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effectSpells.*;
import de.ftscraft.ftskampf.utils.exceptions.EffectNotImplementedException;

public class EffectFactory {
    public static EffectSpell getEffect(String spellEffectID) throws EffectNotImplementedException {
        switch (spellEffectID) {
            case "1":
                return new DamageSpell();
            case "2":
                return new DamageKnockbackSpell();
            case "3":
                return new DamagePenetrationSpell();
            case "4":
                return new DamageAbsorptionSpell();
            case "5":
                return new DamageOverTimeSpell();
            case "6":
                return new HealSpell();
            case "7":
                return new HealLimitRangeSpell();
            case "8":
                return new HealRadiusSpell();
            case "9":
                return new HealSelfDamageSpell();
            case "10":
                return new BuffAttackSpell();
            case "11":
                return new BuffAttackDebuffArmorSpell();
            case "12":
                return new BuffUnarmedSpell();
            case "13":
                return new BuffArmorSpell();
            case "14":
                return new BuffAgilitySpell();
            case "15":
                return new BuffAttackArmorRangeSpell();
            case "16":
                return new ProtectAttackSpell();
            case "17":
                return new DebuffTargetAttackSpell();
            case "18":
                return new DebuffTargetArmorSpell();
            case "19":
                return new DebuffTargetAgilitySpell();
            case "20":
                return new StunSpell();
            case "21":
                return new BlockMagicSpell();
            case "1E":
                return new RaceDamageSpell("E");
            case "1Z":
                return new RaceDamageSpell("Z");
            case "1O":
                return new RaceDamageSpell("O");
            case "22E":
                return new RaceHealSpell("E");
            case "22Z":
                return new RaceHealSpell("Z");
            case "22O":
                return new RaceHealSpell("O");
            case "23E":
                return new RaceBuffAttackRadiusSpell("E");
            case "23Z":
                return new RaceBuffAttackRadiusSpell("Z");
            case "23O":
                return new RaceBuffAttackRadiusSpell("O");
            case "24E":
                return new RaceBuffArmorRadiusSpell("E");
            case "24Z":
                return new RaceBuffArmorRadiusSpell("Z");
            case "24O":
                return new RaceBuffArmorRadiusSpell("O");
            case "25E":
                return new RaceHealRadiusSpell("E");
            case "25Z":
                return new RaceHealRadiusSpell("Z");
            case "25O":
                return new RaceHealRadiusSpell("O");
            default:
                throw new EffectNotImplementedException(STR."Unknown spellEffect id: \{spellEffectID}");
        }
    }
}
