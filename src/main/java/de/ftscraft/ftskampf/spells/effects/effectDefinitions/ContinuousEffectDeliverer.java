package de.ftscraft.ftskampf.spells.effects.effectDefinitions;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.Race;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class ContinuousEffectDeliverer implements Effect {

    protected List<ContinuousEffect> continuousEffects = new ArrayList<>();

    public List<ContinuousEffect> getEffects() {
        return continuousEffects;
    }

    protected void sendEffectConfirmation(ContinuousEffect effect, String casterUUID, String targetUUID) {

        Player caster = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(casterUUID)));
        Player target = Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(targetUUID)));

        FTSKampf plugin = FTSKampf.getPlugin();
        DiceManager diceManager = plugin.getDiceManager();

        Race targetRace = plugin.getRaceOrDefault(target);

        Engine engine = plugin.getEngine();
        Ausweis ausweis = engine.getAusweis(caster);
        Ausweis.Gender gender = ausweis.getGender();
        Race race = plugin.getRaceOrDefault(caster);

        String article = "Der";
        String raceName = race.getmName();
        if (gender.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }

        String articleTarget = "den";
        String raceNameTarget = targetRace.getmName();
        String targetName = target.getName();
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(target);
            if (targetAusweis.getGender().equals(Ausweis.Gender.FEMALE)) {
                articleTarget = "die";
                raceNameTarget = targetRace.getfName();
            }
            targetName = targetAusweis.getFirstName() + " " + targetAusweis.getLastName();
        }
        StringBuilder message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + diceManager.getName(caster) + " §7wirkt §a" + effect.getEffectName() + " §7auf " + articleTarget + " §o" + raceNameTarget + " §e" + targetName);
        diceManager.sendMessageInRange(message, caster);
    }
}