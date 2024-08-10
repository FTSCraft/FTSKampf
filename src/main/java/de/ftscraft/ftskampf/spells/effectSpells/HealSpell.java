package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.spells.effects.Heal;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class HealSpell extends Spell {

    public HealSpell() {
        super("Heilzauber", "0182", "Heilt das Ziel und stellt seine Lebenspunkte wieder her.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new Heal(caster, target);
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
        return true;
    }

    @Override
    public boolean skipsPreDicing() {
        return true;
    }
}
