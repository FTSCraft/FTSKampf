package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.RaceSpell;
import de.ftscraft.ftskampf.spells.effects.RaceBuffAttackRadius;
import org.bukkit.entity.Player;

public class RaceBuffAttackRadiusSpell extends RaceSpell{
    public RaceBuffAttackRadiusSpell(String raceCode) {
        super(
                "Erhöht den Angriff von " + RaceSpell.getPlural2(raceCode),
                "0203" + raceCode,
                "Erhöht den Angriff von " + RaceSpell.getPlural2(raceCode) + " im Radius von 5 Metern.",
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
