package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import org.bukkit.configuration.file.FileConfiguration;

public class ProtectAttack extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public ProtectAttack(String target, String caster) {
        this(target, caster, -1, config.getInt("SPELL_PROTECTATTACK_DURABILITY"));
    }

    public ProtectAttack(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.PROTECT_ATTACK, modifier, durability);

    }

    @Override
    public boolean isProtected() {
        reduceDurability();
        return true;
    }

    @Override
    public String getBuffSign() {
        return "+";
    }

    @Override
    public String getEffectName() {
        return "Immunität";
    }
}