package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class BuffUnarmed extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public BuffUnarmed(String target, String caster) {
        this(target, caster, config.getDouble("SPELL_BUFFUNARMED_MODIFIER"),
                config.getInt("SPELL_BUFFUNARMED_DURABILITY"));
    }

    public BuffUnarmed(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_UNARMED, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        if(!isUnarmed)
            return value;
        reduceDurability();
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "+";
    }

    @Override
    public String getEffectName() {
        return "Unbewaffneter Nahkampf Buff";
    }
}
