package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.BuffArmor;
import de.ftscraft.ftskampf.utils.Message;
import org.bukkit.entity.Player;

public class StunSpell extends EffectSpell {

    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    public StunSpell() {
        super("Betäubungszauber", "0187", "Betäubt das Ziel, sodass es sich für kurze Zeit nicht bewegen oder handeln kann.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        EffectManager effectManager = plugin.getEffectManager();
        diceManager.sendMessageInRange(Message.TAG + "§e" + diceManager.getName(caster) + " §7betäubt §e" + diceManager.getName(target) + " §7für die nächsten drei Runden! (Auf Vertrauensbasis)", caster);
        effectManager.addEffect(new BuffArmor(target.getUniqueId().toString(), caster.getUniqueId().toString()));
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
