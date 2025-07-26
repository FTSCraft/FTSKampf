package de.ftscraft.ftskampf.spells.effects.effectDefinitions;

import java.util.ArrayList;
import java.util.List;

public abstract class ContinuousEffectDeliverer implements Effect {

    protected List<ContinuousEffect> continuousEffects = new ArrayList<>();

    public List<ContinuousEffect> getEffects() {
        return continuousEffects;
    }
}