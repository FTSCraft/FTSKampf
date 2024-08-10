package de.ftscraft.ftskampf.utils;

import org.bukkit.entity.Player;

public class Attack {
    private final Player attacker;
    private final Dice type;
    private final int strength;
    private final double absorptionRate;
    private final boolean penetrateArmor;

    public Attack(Player attacker, Dice type, int strength, double absorptionRate, boolean penetrateArmor) {
        this.attacker = attacker;
        this.type = type;
        this.strength = strength;
        this.penetrateArmor = penetrateArmor;
        this.absorptionRate = absorptionRate;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Dice getType() {
        return type;
    }

    public int getStrength() {
        return strength;
    }

    public double getAbsorptionRate() {
        return absorptionRate;
    }

    public boolean doPenetrateArmor() {return penetrateArmor;}
}
