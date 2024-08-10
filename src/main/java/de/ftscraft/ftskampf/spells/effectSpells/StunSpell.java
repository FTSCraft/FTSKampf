package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.Spell;
import de.ftscraft.ftskampf.utils.Message;
import org.bukkit.entity.Player;

public class StunSpell extends Spell {

    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    public StunSpell() {
        super("Betäubungszauber", "0187", "Betäubt das Ziel, sodass es sich für kurze Zeit nicht bewegen oder handeln kann.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        diceManager.sendMessageInRange(Message.TAG + "§e" + diceManager.getName(caster) + " §7betäubt §e" + diceManager.getName(target) + " §7für die nächsten drei Runden!", caster);
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
