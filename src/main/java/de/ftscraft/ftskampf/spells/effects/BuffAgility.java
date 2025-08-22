package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class BuffAgility extends ContinuousEffect {

    private FTSKampf plugin = FTSKampf.getPlugin();
    private FileConfiguration config = plugin.getConfig();

    public BuffAgility(String target, String caster) {
        this(target, caster, 1.2, 3);
    }

    public BuffAgility(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_AGILITY, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        if(!dice.equals(Dice.AGILITY))
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
        return "Agilitätsbuff";
    }
}