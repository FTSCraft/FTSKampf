package de.ftscraft.ftskampf.spells.effects.effectDefinitions;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Dice;

public abstract class ContinuousEffect implements Effect {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    int durability;
    String target;
    String caster;
    ContinuousEffectId cid;
    protected double modifier;

    public ContinuousEffect(String target, String caster, ContinuousEffectId cid, double modifier, int durability) {
        this.target = target;
        this.caster = caster;
        this.cid = cid;
        this.modifier = modifier;
        this.durability = durability;
    }

    public ContinuousEffectId getCid() {
        return cid;
    }

    public int getDurability() {
        return durability;
    }

    public String getTarget() {
        return target;
    }

    public String getCaster() {
        return caster;
    }

    public double getModifier() {
        return modifier;
    }

    public int modifyDefendValue(int value, Dice dice) {
        return value;
    }

    public int modifyAttackValue(int value, Dice dice, boolean isUnarmed) {
        return value;
    }

    public int returnDamage() {
        return 0;
    }

    public boolean isProtected() {
        return false;
    }

    public abstract String getBuffSign();

    public abstract String getEffectName();

    protected void reduceDurability() {
        durability--;
        if(durability < 1)
            effectManager.removeEffect(this);
    }

    public boolean equals(ContinuousEffect effect) {
        return this.getCid().equals(effect.getCid());
   }
}