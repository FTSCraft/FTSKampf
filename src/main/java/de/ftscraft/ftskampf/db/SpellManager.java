package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.spells.EffectSpell;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.SpellCollection;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpellManager {
    private final FTSKampf plugin = FTSKampf.getPlugin();
    private HashMap<String, SpellCollection> spells;
    List<EffectSpell> allSpells = new ArrayList<>();

    public SpellManager() {
        initClasses();
        initSpells();
    }

    private void initClasses() {

    }

    private void initSpells() {

    }

    public EffectSpell getSpellByZid(String zid) {
        for (EffectSpell spell : allSpells) {
            if (spell.getZid().equals(zid))
                return spell;
        }
        return null;
    }

    public List<EffectSpell> getAllSpells() {
        return allSpells;
    }

    public void playerAddSpell(String uuid, String zid) {
        SpellCollection sc = getSpellCollection(uuid);
        sc.addSpell(zid);
        spells.put(uuid,sc);
        saveZauber();
    }

    public int playerGetNumberOfSpells(String uuid) {
        if(spells.containsKey(uuid))
            return spells.get(uuid).getNumberOfSpells();
        return 0;
    }

    public boolean playerHasSpell(String uuid, EffectSpell spell) {
        return getSpellCollection(uuid).contains(spell);
    }

    public boolean playerResetSpells(String uuid) {
        if(!spells.containsKey(uuid))
            return false;
        spells.remove(uuid);
        saveZauber();
        return true;
    }

    private SpellCollection getSpellCollection(String uuid) {
        SpellCollection collection = spells.get(uuid);
        if (collection == null)
            collection = new SpellCollection(uuid);
        return collection;
    }

    public SpellCollection getSpellCollection(Player player) {
        return getSpellCollection(player.getUniqueId().toString());
    }

    private String getPath() throws IOException {
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

    private HashMap<String, SpellCollection> loadZauber() throws IOException {
        HashMap<String, SpellCollection> spells = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(getPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            String uuid = values[0];
            SpellCollection spellCollection = new SpellCollection(uuid, Arrays.asList(Arrays.copyOfRange(values, 1, values.length)));
            spells.put(uuid, spellCollection);
        }
        reader.close();
        return spells;
    }

    private void saveZauber() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getPath()));
            for (String uuid : spells.keySet()) {
                writer.write(uuid);
                SpellCollection collection = spells.get(uuid);
                for(EffectSpell spell : collection.getSpells())
                    writer.write(";" + spell.getZid());
                writer.newLine();
            }
            writer.close();
        }
        catch (Exception e) {
            saveZauber();
        }
    }

    public void reset() {
        spells.clear();
        saveZauber();
    }
}
