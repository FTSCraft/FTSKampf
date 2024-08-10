package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.spells.effects.Damage;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class DamageSpell extends Spell {

    public DamageSpell() {
        super("Schadenszauber", "0175", "Verursacht direkten Schaden am Ziel.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new Damage(caster, target);
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
