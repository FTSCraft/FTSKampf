package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.main.FTSKampf;

public enum Dice {
    MELEE("Nahkampf", FTSKampf.getPlugin().getConfig().getInt("Dice.Melee.Max"), FTSKampf.getPlugin().getConfig().getInt("Dice.Melee.MaxSkillable")),
    DISTANCE("Fernkampf", FTSKampf.getPlugin().getConfig().getInt("Dice.Distance.Max"), FTSKampf.getPlugin().getConfig().getInt("Dice.Distance.MaxSkillable")),
    MAGIC("Magie", FTSKampf.getPlugin().getConfig().getInt("Dice.Magic.Max"), FTSKampf.getPlugin().getConfig().getInt("Dice.Magic.MaxSkillable")),
    AGILITY("Agilität", FTSKampf.getPlugin().getConfig().getInt("Dice.Agility.Max"), FTSKampf.getPlugin().getConfig().getInt("Dice.Agility.MaxSkillable")),
    ACTION("Aktion", FTSKampf.getPlugin().getConfig().getInt("Dice.Action.Max"), FTSKampf.getPlugin().getConfig().getInt("Dice.Action.Max"));

    private final String name;
    private final int size;
    private final int maxSkillable;

    Dice(String name, int size, int maxSkillable) {
        this.name = name;
        this.size = size;
        this.maxSkillable = maxSkillable;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getMaxSkillable() {
        return maxSkillable;
    }
}
