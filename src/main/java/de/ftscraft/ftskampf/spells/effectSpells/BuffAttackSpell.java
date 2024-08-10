package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.spells.effects.BuffAttack;
import org.bukkit.entity.Player;

public class BuffAttackSpell extends Spell {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    public BuffAttackSpell() {
        super("Angriffsboost", "0171", "Erhöht die Angriffskraft des Anwenders, sodass seine Schläge und Angriffe mehr Schaden verursachen.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        effectManager.addEffect(new BuffAttack(target.getUniqueId().toString(), caster.getUniqueId().toString()));
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
