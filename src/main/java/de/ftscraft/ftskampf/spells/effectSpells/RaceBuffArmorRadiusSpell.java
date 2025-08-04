package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceBuffArmorRadius;
import org.bukkit.entity.Player;

public class RaceBuffArmorRadiusSpell extends RaceSpell {
    public RaceBuffArmorRadiusSpell(String raceCode) {
        super(
                "Erhöht die Rüstung von " + RaceSpell.getPlural2(raceCode),
                "0202" + raceCode,
                "Erhöht die Rüstung von " + RaceSpell.getPlural2(raceCode) + " im Radius von 5 Metern.",
                raceCode
        );
    }


    @Override
    public void doEffect(Player caster, Player target, int value) {
        new RaceBuffArmorRadius(caster.getUniqueId().toString(), race);
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
