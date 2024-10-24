package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.HealRadius;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class HealRadiusSpell extends EffectSpell {

   public HealRadiusSpell() {
        super("Flächenheilung", "0190", "Heilt alle Spieler im Umkreis von 5 Blöcken.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new HealRadius(caster, value);
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
