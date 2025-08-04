package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.main.Logger;
import de.ftscraft.ftskampf.utils.Spell;
import de.ftscraft.ftskampf.utils.SpellClass;
import de.ftscraft.ftskampf.utils.SpellCollection;
import de.ftscraft.ftskampf.utils.exceptions.EffectNotImplementedException;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpellManager {
    private final FTSKampf plugin = FTSKampf.getPlugin();
    private HashMap<String, SpellCollection> spells;
    List<SpellClass> allClasses = new ArrayList<>();

    public SpellManager() {

    }

    public void init() {
        spells = new HashMap<>();
        try {
            initClasses();
            initSpells();
            spells = loadZauber();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initClasses() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getSpellClassPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            SpellClass spellClass = new SpellClass(values[0], values[1], values[2]);
            allClasses.add(spellClass);
        }
        reader.close();
    }

    private void initSpells() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getSpellsPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            SpellClass spellClass = getClassById(values[2]);
            if (spellClass != null) {
                try {
                    Spell spell = new Spell(values[0], values[1], EffectFactory.getEffect(values[3]));
                    spellClass.addSpell(spell);
                } catch (EffectNotImplementedException e) {
                    Logger.log(e.getMessage());
                }
            }
        }
        reader.close();
    }

    public SpellClass getClassById(String id) {
        for (SpellClass spellClass : allClasses) {
            if (spellClass.getId().equals(id)) return spellClass;
        }
        return null;
    }

    public Spell getSpellById(String id) {
        for (SpellClass spellClass : allClasses)
            for (Spell spell : spellClass.getSpells()) {
                if (spell.getId().equals(id)) return spell;
            }
        return null;
    }

    public List<SpellClass> getAllClasses() {
        return allClasses;
    }

    public void playerAddSpell(String uuid, String id) {
        SpellCollection sc = getSpellCollection(uuid);
        sc.addSpell(id);
        spells.put(uuid, sc);
        saveZauber();
    }

    public int playerGetNumberOfSpells(String uuid) {
        if (spells.containsKey(uuid)) return spells.get(uuid).getNumberOfSpells();
        return 0;
    }

    public boolean playerResetSpells(String uuid) {
        if (!spells.containsKey(uuid)) return false;
        spells.remove(uuid);
        saveZauber();
        return true;
    }

    private SpellCollection getSpellCollection(String uuid) {
        SpellCollection collection = spells.get(uuid);
        if (collection == null) collection = new SpellCollection(uuid);
        return collection;
    }

    public SpellCollection getSpellCollection(Player player) {
        return getSpellCollection(player.getUniqueId().toString());
    }

    public boolean playerHasSpell(Player player, Spell spell) {
        return playerHasSpell(player.getUniqueId().toString(), spell);
    }

    public boolean playerHasSpell(String uuid, Spell spell) {
        if(spells.containsKey(uuid))
            return spells.get(uuid).contains(spell);
        return false;
    }

    private String getSavePath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/saves/spells.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    private String getSpellsPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/spells/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/spells/spellDefinitions.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    private String getSpellClassPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/spells/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/spells/spellClasses.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    private HashMap<String, SpellCollection> loadZauber() throws IOException {
        HashMap<String, SpellCollection> spells = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(getSavePath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            String uuid = values[0];
            SpellCollection spellCollection = new SpellCollection(uuid, Arrays.asList(Arrays.copyOfRange(values, 1, values.length)));
            if (spellCollection.getNumberOfSpells() > 0) {
                spells.put(uuid, spellCollection);
            }
        }
        reader.close();
        return spells;
    }

    private void saveZauber() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getSavePath()));
            for (String uuid : spells.keySet()) {
                writer.write(uuid);
                SpellCollection collection = spells.get(uuid);
                for (Spell spell : collection.getSpells())
                    writer.write(";" + spell.getId());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            saveZauber();
        }
    }

    public void reset() {
        spells.clear();
        saveZauber();
    }
}
