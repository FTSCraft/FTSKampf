package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.utils.Message;
import org.bukkit.entity.Player;

public class BlockMagicSpell extends EffectSpell {
    FTSKampf plugin = FTSKampf.getPlugin();
    DiceManager diceManager = plugin.getDiceManager();

    public BlockMagicSpell() {
        super("Blockiert Magie", "0211", "Blockiert die magischen Fähigkeiten eines Ziels, sodass es für kurze Zeit keine Magie nutzen kann.");
    }

    public BlockMagicSpell(String name, String zid, String description) {
        super(name, zid, description);
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {
        diceManager.sendMessageInRange(Message.TAG + "§e" + diceManager.getName(caster) + " §blockiert die magischen Fähigkeiten von §e" + diceManager.getName(target) + " §7für die nächsten drei Runden! (Auf Vertrauensbasis)", caster);
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
