package de.ftscraft.ftskampf.utils;

import java.util.ArrayList;
import java.util.List;

public class SpellClass {
    private String name;
    private String id;
    private List<Spell> spells = new ArrayList<>();
    String race;

    public SpellClass(String name, String id, String race) {
        this.name = name;
        this.id = id;
        this.race = race;
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean raceMatches(String race) {
        return (this.race.toLowerCase().equals(race.toLowerCase()) || this.race.toLowerCase().equals("all"));
    }
}
