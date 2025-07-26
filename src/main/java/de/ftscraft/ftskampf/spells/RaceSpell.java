package de.ftscraft.ftskampf.spells;

public abstract class RaceSpell extends EffectSpell{

    protected String race;

    public RaceSpell(String name, String zid, String description, String raceCode) {
        super(name, zid, description);
        initRace(raceCode);
    }

    public static String getPlural1(String code) {
        return switch (code) {
            case "Z" -> "Zwerge";
            case "O" -> "Orks";
            case "E" -> "Elfen";
            default  -> "Unbekannt";
        };
    }
    public static String getPlural2(String code) {
        return switch (code) {
            case "Z" -> "Zwergen";
            case "O" -> "Orks";
            case "E" -> "Elfen";
            default  -> "Unbekannt";
        };
    }

    protected void initRace(String raceCode) {
        switch (raceCode) {
            case "Z":
                race = "Zwerg";
                break;
            case "O":
                race = "Ork";
                break;
            case "E":
                race = "Elf";
                break;
        }
    }
}
