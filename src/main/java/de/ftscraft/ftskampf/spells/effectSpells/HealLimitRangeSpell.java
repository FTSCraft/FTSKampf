package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.spells.effects.HealLimitRange;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class HealLimitRangeSpell extends Spell {

    public HealLimitRangeSpell() {
        super("Heilung in der Nähe", "0183", "Heilt ein Ziel besser als normal, jedoch nur in einer Reichweite von 2 Meter.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new HealLimitRange(caster, target);
        } catch (RaceDoNotExistException e) {
            //Sollte nicht dazu kommen da vorher abgefangen bei Würfelwurf auf Target
            //Wenn doch Melleming hauen
        }
    }

    @Override
    public boolean isSelfApplicable() {
        return false;
    }

    @Override
    public boolean isTargetApplicable() {
        return true;
    }

    @Override
    public boolean skipsPreDicing() {
        return true;
    }
}
