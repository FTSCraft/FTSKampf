package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.Damage;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class DamageKnockbackSpell extends EffectSpell {
    public DamageKnockbackSpell() {
        super("Schadenszauber mit Rückstoß", "0215", "Verursacht direkten Schaden am Ziel und stößt es zurück.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new Damage(caster, target, 1.4);
            FTSKampf.getPlugin().getDiceManager().sendMessageInRange(STR."\{Message.TAG}§6\{target.getName()} §awird 3 Blöcke zurückgestoßen!", target);
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
