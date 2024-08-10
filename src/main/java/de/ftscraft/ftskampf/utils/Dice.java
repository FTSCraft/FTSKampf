package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.main.FTSKampf;

public enum Dice {
    MELEE("Nahkampf", FTSKampf.getPlugin().getConfig().getInt("Dice.Melee.Max")),
    DISTANCE("Fernkampf", FTSKampf.getPlugin().getConfig().getInt("Dice.Distance.Max")),
    MAGIC("Magie", FTSKampf.getPlugin().getConfig().getInt("Dice.Magic.Max")),
    AGILITY("Agilität", FTSKampf.getPlugin().getConfig().getInt("Dice.Agility.Max")),
    ACTION("Aktion", FTSKampf.getPlugin().getConfig().getInt("Dice.Action.Max"));

    private final String name;
    private final int size;

    Dice(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

}
