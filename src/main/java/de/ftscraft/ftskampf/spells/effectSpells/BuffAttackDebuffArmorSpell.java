package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.BuffAttackDebuffArmor;
import org.bukkit.entity.Player;

public class BuffAttackDebuffArmorSpell extends EffectSpell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public BuffAttackDebuffArmorSpell() {
        super("Kraftvoller Angriff, Schwache Rüstung", "0173", "Erhöht die Angriffskraft des Zeils stark, schwächt jedoch gleichzeitig seine Rüstung.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        BuffAttackDebuffArmor conEffect = new BuffAttackDebuffArmor(target.getUniqueId().toString(), caster.getUniqueId().toString());
        effectManager.addEffect(conEffect.getEffects());
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
