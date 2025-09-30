package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectDeliverer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BuffAttackArmorRange extends ContinuousEffectDeliverer {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    private final int RANGE = config.getInt("SPELL_BBUFFATTACKARMORRANG_RANGE");

    public BuffAttackArmorRange(String target, String caster) {
        for (Player effTarget : getTargets(Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(target))))) {
            BuffAttack buffAttack = new BuffAttack(effTarget.getUniqueId().toString(), caster, config.getDouble("SPELL_BUFFATTACKARMORRANGE_MODIFIER_ATTACK"));
            continuousEffects.add(buffAttack);
            BuffArmor buffArmor = new BuffArmor(effTarget.getUniqueId().toString(), caster, config.getDouble("SPELL_BUFFATTACKARMORRANGE_MODIFIER_ARMOR"));
            continuousEffects.add(buffArmor);
            sendEffectConfirmation(buffAttack, caster, effTarget.getUniqueId().toString());
            sendEffectConfirmation(buffArmor, caster, effTarget.getUniqueId().toString());
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
