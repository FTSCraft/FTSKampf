package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceHealRadius;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class RaceHealRadiusSpell extends RaceSpell {
    public RaceHealRadiusSpell(String raceCode) {
        String pl1 = RaceSpell.getPlural1(raceCode);
        super(
                STR."Flächenheilung für \{pl1}",
                STR."0205\{raceCode}",
                STR."Heilt alle \{pl1} im Umkreis.",
                raceCode
        );
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new RaceHealRadius(caster, value, race);
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
        return false;
    }

    @Override
    public boolean skipsPreDicing() {
        return false;
    }
}
