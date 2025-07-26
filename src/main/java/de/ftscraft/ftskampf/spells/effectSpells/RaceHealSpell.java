package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceHeal;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class RaceHealSpell extends RaceSpell {
    public RaceHealSpell(String raceCode) {
        super(
                "Heilzauber für " + RaceSpell.getPlural1(raceCode),
                "0204" + raceCode,
                "Stärkere Heilung für " + RaceSpell.getPlural1(raceCode) + ".",
                raceCode
        );
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new RaceHeal(caster, target, race);
        } catch (RaceDoNotExistException e) {
            //Sollte nicht dazu kommen da vorher abgefangen bei Würfelwurf auf Target
            //Wenn doch Melleming hauen
        }
    }

    @Override
    public boolean isSelfApplicable() {
        return true;
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
