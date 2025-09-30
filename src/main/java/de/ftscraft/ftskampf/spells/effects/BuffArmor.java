package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class BuffArmor extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public BuffArmor(String target, String caster) {
        this(target, caster, config.getDouble("SPELL_BUFFARMOR_MODIFIER"),
                config.getInt("SPELL_BUFFARMOR_DURABILITY"));
    }

    public BuffArmor(String target, String caster, double modifier) {
        this(target, caster, modifier, 3);
    }

    public BuffArmor(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_ARMOR, modifier, durability);
    }

    @Override
    public int modifyDefendValue(int value, Dice dice) {
        super.reduceDurability();
        return (int) Math.round(modifier * value);
    }

    @Override
    public String getBuffSign() {
        return "+";
    }

    @Override
    public String getEffectName() {
        return "Verteidigungsbuff";
    }
}