package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.DebuffTargetAgility;
import org.bukkit.entity.Player;

public class DebuffTargetAgilitySpell extends EffectSpell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public DebuffTargetAgilitySpell() {
        super("Agilitätsschwächung", "0179", "Verringert die Agilität des Ziels, wodurch es sich langsamer und ungeschickter bewegt.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        effectManager.addEffect(new DebuffTargetAgility(target.getUniqueId().toString(), caster.getUniqueId().toString()));
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
