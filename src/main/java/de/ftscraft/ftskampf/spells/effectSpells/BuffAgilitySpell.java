package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.BuffAgility;
import org.bukkit.entity.Player;

public class BuffAgilitySpell extends EffectSpell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public BuffAgilitySpell() {
        super("Agilitätsboost", "0169", "Erhöht die Agilität des Anwenders, wodurch er sich schneller und geschickter im Kampf bewegen kann.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        effectManager.addEffect(new BuffAgility(target.getUniqueId().toString(), caster.getUniqueId().toString()));
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
        return false;
    }
}
