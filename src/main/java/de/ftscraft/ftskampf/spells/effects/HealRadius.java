package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.Effect;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.Race;
import de.ftscraft.ftskampf.utils.exceptions.NumberNegativeException;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HealRadius implements Effect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();
    Engine engine = plugin.getEngine();
    DiceManager diceManager = plugin.getDiceManager();
    HpManager hpManager = plugin.getHpManager();

    public HealRadius(Player player, int value) throws RaceDoNotExistException {
        List<Player> targets = new ArrayList<>();
        int RANGE = config.getInt("SPELL_HEALRADIUS_RANGE");
        for (Entity nearbyEntity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), RANGE, RANGE, RANGE)) {
            if (nearbyEntity instanceof Player) {
                targets.add((Player) nearbyEntity);
            }
        }
        double modifier = config.getDouble("SPELL_HEALRADIUS_MODIFIER");
        int healPoints = (int) Math.round(modifier * value);
        diceManager.sendMessageInRange(Message.TAG + "§7Geheilt werden §c" + healPoints + " §7LP!",player);
        for(Player target : targets) {
            try {
                hpManager.healPlayer(target,healPoints);
            } catch (NumberNegativeException ignored) {
            }

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
                message = new StringBuilder("§r§e" + target.getName() + " §7wurde geheilt!");
            }

            diceManager.sendMessageInRange(message, player);

            if (hpManager.isMaxHealthReached(target)) {
                target.sendMessage(Message.TAG + "§7Du bist nun vollständig geheilt!");
            } else {
                target.sendMessage(Message.TAG + "§7Du hast nun §c" + hpManager.getHealth(target) + " §7Lebenspunkte!");
            }
        }
    }
}