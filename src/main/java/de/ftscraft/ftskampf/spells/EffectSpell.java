package de.ftscraft.ftskampf.spells;

import org.bukkit.entity.Player;

public abstract class EffectSpell {
    private String zid;
    private String name;
    private String description;
    private String race;

    public String getZid() {
        return zid;
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean equals(EffectSpell compare) {
        if(compare == null) return false;
        return compare.getZid() == zid;
    }

    public EffectSpell(String name, String zid, String description) {
        this.name = name;
        this.zid = zid;
        this.description = description;
        this.race = "all";
    }

    public EffectSpell(String name, String zid, String description, String race) {
        this.name = name;
        this.zid = zid;
        this.description = description;
        this.race = race;
    }

    public abstract void doEffect(Player caster, Player target, int value);

    public double modifyAttack(double initialAttack) {
        return initialAttack;
    }

    public double modifyDefend(double initialDefend) {
        return initialDefend;
    }

    public boolean raceMatches(String race) {
        return this.race.toLowerCase() == race.toLowerCase() || this.race.toLowerCase() == "all";
    }

    public abstract boolean isSelfApplicable();
    public abstract boolean isTargetApplicable();
    public abstract boolean skipsPreDicing();
}
