package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.Damage;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DamageKnockbackSpell extends EffectSpell {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public DamageKnockbackSpell() {
        super("Schadenszauber mit Rückstoß", "0215", "Verursacht direkten Schaden am Ziel und stößt es zurück.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        try {
            new Damage(caster, target, config.getDouble("SPELL_DAMAGEKNOCKBACK_MODIFIER"));
            //FTSKampf.getPlugin().getDiceManager().sendMessageInRange(Message.TAG + "§6" + target.getName() + " §awird 3 Blöcke zurückgestoßen!", target);
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
