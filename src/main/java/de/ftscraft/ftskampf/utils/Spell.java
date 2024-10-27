package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.spells.EffectSpell;
import org.bukkit.entity.Player;

public class Spell {
    private String name;
    private String id;
    private EffectSpell effect;

    public Spell(String name, String id, EffectSpell effect) {
        this.name = name;
        this.id = id;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public EffectSpell getEffect() {
        return effect;
    }

    public void doEffect(Player player, Player target, int value) {
        effect.doEffect(player, target, value);
    }

    public String getDescription() {
        return effect.getDescription();
    }

    public boolean equals(Spell compare) {
        if(compare == null) return false;
        return compare.getId().equals(id);
    }
}
