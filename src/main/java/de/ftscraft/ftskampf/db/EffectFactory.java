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
                return new ProtectAttackSpell();
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
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "17":
                return new DebuffTargetAttackSpell();
            case "18":
                return new DebuffTargetArmorSpell();
            case "19":
                return new DebuffTargetAgilitySpell();
            case "20":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "21":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "1E":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "1Z":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "1O":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "22E":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "23E":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "24E":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "25E":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "22Z":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "23Z":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "24Z":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "25Z":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "22O":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "23O":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "24O":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            case "25O":
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
            default:
                throw new EffectNotImplementedException("Unknown spellEffect id: " + spellEffectID);
        }
    }
}
