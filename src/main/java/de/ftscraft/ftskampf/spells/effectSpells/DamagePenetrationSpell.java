package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.DamagePenetration;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class DamagePenetrationSpell extends EffectSpell {

    public DamagePenetrationSpell() {
        super("Rüstungsdurchdringender Schaden", "0178", "Verursacht Schaden, der die Rüstung des Ziels durchdringt.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new DamagePenetration(caster, target);
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
