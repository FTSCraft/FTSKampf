package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.DamageAbsorption;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class DamageAbsorptionSpell extends EffectSpell {

    public DamageAbsorptionSpell() {
        super("Absorptionszauber", "0176", "Absorbiert Leben vom Ziel zum Anwender.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new DamageAbsorption(caster, target);
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
