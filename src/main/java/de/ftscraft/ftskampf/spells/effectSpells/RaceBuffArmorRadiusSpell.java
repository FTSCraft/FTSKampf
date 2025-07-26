package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceBuffAttackRadius;
import org.bukkit.entity.Player;

public class RaceBuffArmorRadiusSpell extends RaceSpell {
    public RaceBuffArmorRadiusSpell(String raceCode) {
        String pl2 = RaceSpell.getPlural2(raceCode);
        super(
                STR."Erhöht die Rüstung von \{pl2}",
                STR."0202\{raceCode}",
                STR."Erhöht die Rüstung von \{pl2} im Radius von 5 Metern.",
                raceCode
        );
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        new RaceBuffAttackRadius(caster.getUniqueId().toString(), race);
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
