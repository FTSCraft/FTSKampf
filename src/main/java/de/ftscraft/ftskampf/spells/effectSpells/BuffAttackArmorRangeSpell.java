package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.spells.effects.BuffAttackArmorRange;
import org.bukkit.entity.Player;

public class BuffAttackArmorRangeSpell extends Spell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public BuffAttackArmorRangeSpell() {
        super("Angriff und Rüstungswert im Umkreis leicht erhöht", "0172", "Erhöht die Angriffskraft und die Rüstung von allen in nächster Umgebung leicht.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        BuffAttackArmorRange conEffect = new BuffAttackArmorRange(target.getUniqueId().toString(), caster.getUniqueId().toString());
        effectManager.addEffect(conEffect.getEffects());
    }

    @Override
    public boolean isSelfApplicable() {
        return true;
    }

    @Override
    public boolean isTargetApplicable() {
        return false;
    }

    @Override
    public boolean skipsPreDicing() {
        return false;
    }
}
