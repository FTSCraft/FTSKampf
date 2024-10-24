package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.db.SpellManager;
import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.main.FTSKampf;

import java.util.ArrayList;
import java.util.List;

public class SpellCollection {
    FTSKampf plugin = FTSKampf.getPlugin();
    SpellManager spellManager = plugin.getSpellManager();
    String owner;
    List<EffectSpell> spells;


    public SpellCollection(String owner) {
        this.owner = owner;
        spells = new ArrayList<>();
    }

    public SpellCollection(String owner, List<String> spells) {
        this.owner = owner;
        this.spells = new ArrayList<>();
        for(String zid : spells) {
            this.spells.add(spellManager.getSpellByZid(zid));
        }
    }

    public void addSpell(String zid) {
        spells.add(spellManager.getSpellByZid(zid));
    }

    public List<EffectSpell> getSpells() {
        return spells;
    }

    public int getNumberOfSpells() {
        return spells.size();
    }

    public boolean contains(EffectSpell spell) {
        for(EffectSpell vSpell : spells) {
            if (vSpell.equals(spell))
                return true;
        }
        return false;
    }
}
