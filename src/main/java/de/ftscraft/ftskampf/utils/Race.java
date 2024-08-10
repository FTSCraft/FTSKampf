package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.configuration.file.FileConfiguration;

public class Race {
    private String mName;
    private String fName;
    private int points;
    private int melee;
    private int distance;
    private int magic;
    private int agility;
    private int health;

    public Race(String confName) {
        String race = "Races." + confName;
        FileConfiguration config = FTSKampf.getPlugin().getConfig();
        this.mName = config.getString(race + ".MName");
        this.fName = config.getString(race + ".FName");
        this.points = config.getInt(race + ".points");
        race += ".InitialValues.";
        this.melee = config.getInt(race + "Melee");
        this.distance = config.getInt(race + "Distance");
        this.magic = config.getInt(race + "Magic");
        this.agility = config.getInt(race + "Agility");
        this.health = config.getInt(race + "Health");
    }

    public String getmName() {
        return mName;
    }

    public String getfName() {
        return fName;
    }

    public int getPoints() {
        return points;
    }

    public int getSkill(Dice dice) {
        switch (dice) {
            case MELEE:
                return getMelee();
            case DISTANCE:
                return getDistance();
            case AGILITY:
                return getAgility();
            case MAGIC:
                return getMagic();
            default:
                return 0;
        }
    }

    public int getMelee() {
        return melee;
    }

    public int getDistance() {
        return distance;
    }

    public int getMagic() {
        return magic;
    }

    public int getAgility() {
        return agility;
    }

    public int getHealth() {
        return health;
    }
}
