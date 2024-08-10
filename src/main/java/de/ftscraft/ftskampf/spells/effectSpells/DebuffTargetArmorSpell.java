package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.spells.effects.DebuffTargetArmor;
import org.bukkit.entity.Player;

public class DebuffTargetArmorSpell extends Spell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public DebuffTargetArmorSpell() {
        super("Rüstungsschwächung", "0180", "Verringert die Rüstung des Ziels, wodurch es anfälliger für Schaden wird.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        effectManager.addEffect(new DebuffTargetArmor(target.getUniqueId().toString(), caster.getUniqueId().toString()));
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
