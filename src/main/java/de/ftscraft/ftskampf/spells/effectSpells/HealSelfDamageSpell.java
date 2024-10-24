package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.HealSelfDamage;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class HealSelfDamageSpell extends EffectSpell {

    public HealSelfDamageSpell() {
        super("Starke Heilung mit Schaden am Anwender", "0185", "Heilt ein Ziel stark, fügt jedoch dem Anwender selbst Schaden zu.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new HealSelfDamage(caster, target, value);
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
        return false;
    }
}
