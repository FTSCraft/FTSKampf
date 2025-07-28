package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectDeliverer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RaceBuffAttackRadius extends ContinuousEffectDeliverer {

    private final int RANGE = 5;
    EffectManager effectManager = FTSKampf.getPlugin().getEffectManager();

    public RaceBuffAttackRadius(String caster, String raceName) {
        for (Player effTarget : getTargets(Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(caster))), raceName)) {
            BuffAttack buffAttack = new BuffAttack(effTarget.getUniqueId().toString(), caster, 1.5);
            effectManager.addEffect(buffAttack);
            sendEffectConfirmation(buffAttack, caster, effTarget.getUniqueId().toString());
        }
    }

    private List<Player> getTargets(Player target, String raceName) {
        List<Player> targets = new ArrayList<>();
        for (Entity nearbyEntity : target.getLocation().getWorld().getNearbyEntities(target.getLocation(), RANGE, RANGE, RANGE)) {
            if (nearbyEntity instanceof Player) {
                if(checkTargetRace(raceName, target))
                    targets.add((Player) nearbyEntity);
            }
        }
        return targets;
    }

    private boolean checkTargetRace(String raceName, Player target) {
        FTSKampf plugin = FTSKampf.getPlugin();
        return plugin.getRaceOrDefault(target).getmName().equals(raceName);
    }
}
