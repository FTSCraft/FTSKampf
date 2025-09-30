package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.configuration.file.FileConfiguration;

public class BuffAttack extends ContinuousEffect {

    private static final FTSKampf plugin = FTSKampf.getPlugin();
    private static final FileConfiguration config = plugin.getConfig();

    public BuffAttack(String target, String caster) {
        this(target, caster, config.getDouble("SPELL_BUFFATTACK_MODIFIER"),
                config.getInt("SPELL_BUFFATTACK_DURABILITY"));
    }

    public BuffAttack(String target, String caster, double modifier) {
        this(target, caster, modifier, config.getInt("SPELL_BUFFATTACK_MODIFIER"));
    }

    public BuffAttack(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.BUFF_ATTACK, modifier, durability);
    }

    @Override
    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        if(dice.equals(Dice.AGILITY))
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
        return "Angriffsbuff";
    }
}