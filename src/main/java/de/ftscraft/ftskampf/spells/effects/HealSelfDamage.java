package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.Effect;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.Race;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.entity.Player;

public class HealSelfDamage implements Effect {

    FTSKampf plugin = FTSKampf.getPlugin();
    Engine engine = plugin.getEngine();
    DiceManager diceManager = plugin.getDiceManager();
    HpManager hpManager = plugin.getHpManager();

    private final double healModifier = 1.5;
    private final double damageModifier = 0.5;

    public HealSelfDamage(Player player, Player target, int value) throws RaceDoNotExistException {

        int healPoints = (int) Math.round(healModifier * value);
        diceManager.sendMessageInRange(Message.TAG + "§7Geheilt werden §c" + healPoints + " §7LP!", player);

        hpManager.healPlayer(target, healPoints);
        Race race = plugin.getRaceOrDefault(target);
        String article = "Der";
        String raceName = race.getmName();
        StringBuilder message;
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(target);
            Ausweis.Gender tarGender = targetAusweis.getGender();
            if (tarGender.equals(Ausweis.Gender.FEMALE)) {
                article = "Die";
                raceName = race.getfName();
            }
            message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(target) + " §7wurde geheilt!");
        } else {
            message = new StringBuilder("§r§e" + diceManager.getName(target) + " §7wurde geheilt!");
        }

        diceManager.sendMessageInRange(message, player);

        if (hpManager.isMaxHealthReached(target)) {
            target.sendMessage(Message.TAG + "§7Du bist nun vollständig geheilt!");
        } else {
            target.sendMessage(Message.TAG + "§7Du hast nun §c" + hpManager.getHealth(target) + " §7Lebenspunkte!");
        }

        race = plugin.getRace(player);
        article = "Der";
        raceName = race.getmName();
        Ausweis ausweis = engine.getAusweis(player);
        Ausweis.Gender gender = ausweis.getGender();
        if (ausweis.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }
        int damage = (int) Math.round(damageModifier * value);
        hpManager.hurtPlayer(player, damage);
        message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(target) + " §7fügt sich dabei §c" + damage + " §7LP Schaden zu!");
        diceManager.sendMessageInRange(message, player);

        if (hpManager.getHealth(target) <= 0) {
            diceManager.sendMessageInRange(Message.TAG + "§c" + diceManager.getName(player) + " §7ist kampfunfähig!", target);
        } else {
            diceManager.sendMessageInRange(Message.TAG + "§c" + diceManager.getName(player) + " §7hat nun §c" + hpManager.getHealth(target) + " §7HP!", target);
        }
    }
}