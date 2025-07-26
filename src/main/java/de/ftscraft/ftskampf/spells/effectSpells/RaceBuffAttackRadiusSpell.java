package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import org.bukkit.entity.Player;

public class RaceBuffAttackRadiusSpell extends RaceSpell{
    public RaceBuffAttackRadiusSpell(String raceCode) {
        String pl2 = RaceSpell.getPlural2(raceCode);
        super(
                STR."Erhöht den Angriff von \{pl2}",
                STR."0203\{raceCode}",
                STR."Erhöht den Angriff von \{pl2} im Radius von 5 Metern.",
                raceCode
        );
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {

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
