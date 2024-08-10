package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;

public class LivingEffect {
    ContinuousEffect effect;
    int lifetime;
    FTSKampf plugin = FTSKampf.getPlugin();

    public LivingEffect(ContinuousEffect effect, int lifetime) {
        this.effect = effect;
        this.lifetime = lifetime;
    }

    public ContinuousEffect getEffect() {
        return effect;
    }

    public int getLifetime() {
        return lifetime;
    }

    public boolean iterateLifetime() {
        return ++lifetime >= plugin.getConfig().getInt("EffectLifetime");
    }
}
