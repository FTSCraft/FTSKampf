package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.DamageOverTime;
import org.bukkit.entity.Player;

public class DamageOverTimeSpell extends EffectSpell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public DamageOverTimeSpell() {
        super("Schaden über Zeit", "0177", "Verursacht Schaden am Ziel für die nächsten drei Angriffe.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        effectManager.addEffect(new DamageOverTime(target.getUniqueId().toString(), caster.getUniqueId().toString()));
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