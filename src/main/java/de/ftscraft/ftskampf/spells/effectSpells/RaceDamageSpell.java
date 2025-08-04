package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceDamage;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class RaceDamageSpell extends RaceSpell {

    public RaceDamageSpell(String raceCode) {
        super(
                "Schadenszauber gegen " + RaceSpell.getPlural1(raceCode),
                "0201" + raceCode,
                "Verursacht stärkeren Schaden an " + RaceSpell.getPlural2(raceCode) + ".",
                raceCode
        );
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new RaceDamage(caster, target, race);
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
