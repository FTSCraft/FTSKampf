package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceDamage;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class RaceDamageSpell extends RaceSpell {

    public RaceDamageSpell(String raceCode) {
        String pl1 = RaceSpell.getPlural1(raceCode);
        String pl2 = RaceSpell.getPlural2(raceCode);
        super(
                STR."Schadenszauber gegen \{pl1}",
                STR."0201\{raceCode}",
                STR."Verursacht stärkeren Schaden an \{pl2}.",
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
