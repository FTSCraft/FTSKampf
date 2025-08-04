package de.ftscraft.ftskampf.spells.effects;

import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;

public class DamageOverTime extends ContinuousEffect {

    FTSKampf plugin = FTSKampf.getPlugin();
    //HpManager hpManager = plugin.getHpManager();

    private int damage;

    public DamageOverTime(String target, String caster) {
        this(target, caster, -1, 3);
    }

    public DamageOverTime(String target, String caster, double modifier, int durability) {
        super(target, caster, ContinuousEffectId.DAMAGE_OVER_TIME, modifier, durability);
        damage = 12;
        //damage = (int) Math.round((double) hpManager.getMaxHealth(Bukkit.getOfflinePlayer(target)) / 3); wäre ne Idee, Engine Schnittstelle prüfen
    }

    @Override
    public int returnDamage() {
        reduceDurability();
        return damage;
    }

    @Override
    public String getBuffSign() {
        return "-";
    }

    @Override
    public String getEffectName() {
        return "Schaden über Zeit";
    }
}