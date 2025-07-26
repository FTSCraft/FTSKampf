package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectDeliverer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BuffAttackArmorRange extends ContinuousEffectDeliverer {

    private final int RANGE = 5;

    public BuffAttackArmorRange(String target, String caster) {
        for (Player effTarget : getTargets(Bukkit.getPlayer(target))) {
            continuousEffects.add(new BuffAttack(effTarget.getUniqueId().toString(), caster, 1.2));
            continuousEffects.add(new BuffArmor(effTarget.getUniqueId().toString(), caster, 1.2));
        }

    }

    private List<Player> getTargets(Player target) {
        List<Player> targets = new ArrayList<>();
        for (Entity nearbyEntity : target.getLocation().getWorld().getNearbyEntities(target.getLocation(), RANGE, RANGE, RANGE)) {
            if (nearbyEntity instanceof Player) {
                targets.add((Player) nearbyEntity);
            }
        }
        return targets;
    }
}
