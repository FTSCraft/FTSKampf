package de.ftscraft.ftskampf.spells.effectSpells;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.spells.effects.ProtectAttack;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.Race;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class ProtectAttackSpell extends EffectSpell {

    private final double damageModifier = 0.5;

    public ProtectAttackSpell() {
        super("Schutz vor Angriff", "0186", "Schützt das Ziel vor dem nächsten Angriff, fügt jedoch dem Anwender Schaden zu.");
    }

    @Override
    public void doEffect(Player caster, Player target, int value) {

        FTSKampf plugin = FTSKampf.getPlugin();
        DiceManager diceManager = plugin.getDiceManager();
        Engine engine = plugin.getEngine();
        EffectManager effectManager = plugin.getEffectManager();
        HpManager hpManager = plugin.getHpManager();

        effectManager.addEffect(new ProtectAttack(target.getUniqueId().toString(), caster.getUniqueId().toString()));
        //Self Damage
        Race race;
        race = plugin.getRaceOrDefault(caster);
        String article = "Der";
        String raceName = race.getmName();
        Ausweis ausweis = engine.getAusweis(caster);
        Ausweis.Gender gender = ausweis.getGender();
        if (gender.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }
        int damage = (int) Math.round(damageModifier * value);

        hpManager.hurtPlayer(caster, damage);
        StringBuilder message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(caster) + " §7fügt sich dabei §c" + damage + " §7LP Schaden zu!");
        diceManager.sendMessageInRange(message, caster);

        if (hpManager.getHealth(target) <= 0) {
            diceManager.sendMessageInRange(Message.TAG + "§c" + diceManager.getName(caster) + " §7ist kampfunfähig!", caster);
        } else {
            diceManager.sendMessageInRange(Message.TAG + "§c" + diceManager.getName(caster) + " §7hat nun §c" + hpManager.getHealth(caster) + " §7HP!", caster);
        }

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
