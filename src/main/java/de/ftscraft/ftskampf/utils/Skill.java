package de.ftscraft.ftskampf.utils;

public class Skill {
    private final String uuid;
    private int points;
    private int melee;
    private int distance;
    private int magic;
    private int agility;
    private int maxHp;

    public Skill(String uuid, int points, int melee, int distance, int magic, int agility, int maxHp) {
        this.uuid = uuid;
        this.points = points;
        this.melee = melee;
        this.distance = distance;
        this.magic = magic;
        this.agility = agility;
        this.maxHp = maxHp;
    }

    public Skill(String uuid, int points) {
        this.uuid = uuid;
        this.points = points;
        this.melee = 0;
        this.distance = 0;
        this.magic = 0;
        this.agility = 0;
        this.maxHp = 0;
    }

    public String getUUID() {
        return uuid;
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

    public int getMaxHp() { return maxHp; }

    public void setSkill(Dice dice, int points) {
        switch (dice) {
            case MELEE:
                setMelee(points);
                return;
            case DISTANCE:
                setDistance(points);
                return;
            case AGILITY:
                setAgility(points);
                return;
            case MAGIC:
                setMagic(points);
                return;
            default:
        }
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setMelee(int melee) {
        this.melee = melee;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
}
