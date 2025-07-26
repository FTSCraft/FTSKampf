package de.ftscraft.ftskampf.spells;

import org.bukkit.entity.Player;

public abstract class EffectSpell {
    private String zid;
    private String name;
    private String description;

    public String getZid() {
        return zid;
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public EffectSpell(String name, String zid, String description) {
        this.name = name;
        this.zid = zid;
        this.description = description;
    }

    public abstract void doEffect(Player caster, Player target, int value);

    public abstract boolean isSelfApplicable();
    public abstract boolean isTargetApplicable();
    public abstract boolean skipsPreDicing();
}
