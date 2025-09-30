package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectDeliverer;
import org.bukkit.configuration.file.FileConfiguration;

public class BuffAttackDebuffArmor extends ContinuousEffectDeliverer {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public BuffAttackDebuffArmor(String target, String caster) {
        continuousEffects.add(new BuffAttack(target, caster, config.getDouble("SPELL_BUFFATTACKDEBUFFARMOR_MODIFIER_ATTACK")));
        continuousEffects.add(new DebuffTargetArmor(target, caster, config.getDouble("SPELL_BUFFATTACKDEBUFFARMOR_MODIFIER_ARMOR")));
    }
}
