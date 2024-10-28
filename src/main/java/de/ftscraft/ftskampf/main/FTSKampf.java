package de.ftscraft.ftskampf.main;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftskampf.commands.*;
import de.ftscraft.ftskampf.damageCalculators.DiceManager;
import de.ftscraft.ftskampf.db.*;
import de.ftscraft.ftskampf.listeners.InventoryListener;
import de.ftscraft.ftskampf.listeners.JoinListener;
import de.ftscraft.ftskampf.utils.HealthRunner;
import de.ftscraft.ftskampf.utils.LivingEffectRunner;
import de.ftscraft.ftskampf.utils.MappedInventory;
import de.ftscraft.ftskampf.utils.Race;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class FTSKampf extends JavaPlugin {

    private static FTSKampf plugin;
    private static ConfigManager configManager;
    private static DiceManager diceManager;
    public static List<Inventory> kampfInventorys;
    public static List<MappedInventory> spellChooseInventory;//contains inventory and zid mapping
    public static List<MappedInventory> spellCastInventory;
    public static HashMap<MappedInventory, Player> spellTargetInventory;
    public static HashMap<Inventory, Player> targetInventorys;
    public static List<String> raceList;
    private Engine engine;
    private DBManager dbManager;
    private HpManager hpManager;
    private EffectManager effectManager;
    private SpellManager spellManager;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager();
        kampfInventorys = new ArrayList<>();
        spellChooseInventory = new ArrayList<>();
        spellCastInventory = new ArrayList<>();
        spellTargetInventory = new HashMap<>();
        targetInventorys = new HashMap<>();
        raceList = getRaceList();

        engine = (Engine) getServer().getPluginManager().getPlugin("FTSEngine");
        effectManager = new EffectManager();
        effectManager.init();
        spellManager = new SpellManager();
        dbManager = new DBManager();
        hpManager = new HpManager();
        diceManager = new DiceManager();

        configManager.checkDatabaseVersions();

        getCommand("kampf").setExecutor(new CMDKampf());
        getCommand("hp").setExecutor(new CMDHp());
        getCommand("heilen").setExecutor(new CMDHeal());
        getCommand("kampfskill").setExecutor(new CMDKampfskill());
        if (getConfig().getBoolean("Testmode")) {
            getCommand("hurt").setExecutor(new CMDHurt());
        }
        getCommand("react").setExecutor(new CMDReact());
        getCommand("magie").setExecutor(new CMDMagie());
        getCommand("ftskampfdb").setExecutor(new CMDftskampfdb());

        InventoryListener inventoryListener = new InventoryListener();
        getServer().getPluginManager().registerEvents(inventoryListener, plugin);
        JoinListener joinListener = new JoinListener();
        getServer().getPluginManager().registerEvents(joinListener, plugin);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new HealthRunner(), 20L, 20L);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new LivingEffectRunner(), 20L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static FTSKampf getPlugin() {
        return plugin;
    }

    public DBManager getDB() {
        return dbManager;
    }

    public Engine getEngine() {
        return engine;
    }

    public HpManager getHpManager() {
        return hpManager;
    }

    public DiceManager getDiceManager() {
        return diceManager;
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }

    public Race getRaceOrDefault(Player player) {
        FileConfiguration config = plugin.getConfig();
        Race race;
        if (!engine.hasAusweis(player)) {
            return new Race(config.getString("Races.Default"));
        }
        try {
            race = getRace(player);
        } catch (RaceDoNotExistException e) {
            return new Race(config.getString("Races.Default"));
        }
        return race;
    }

    public Race getRace(Player player) throws RaceDoNotExistException {
        String race = findRace(engine.getAusweis(player).getRace());
        FileConfiguration config = plugin.getConfig();
        if ((config.getString("Races." + race) == null)) {
            throw new RaceDoNotExistException("Rasse nicht gefunden!");
        }
        return new Race(race);
    }

    private String findRace(String race) {
        int found = 0;
        String foundRace = null;
        for (String existingRace : raceList) {
            if (race.toLowerCase().contains(existingRace.toLowerCase())) {
                found++;
                foundRace = existingRace;
            }
        }
        if (found == 1) {
            return foundRace;
        }
        return null;
    }

    private List<String> getRaceList() {
        List<String> raceList = new ArrayList<>();
        FileConfiguration config = plugin.getConfig();
        for (String race : config.getStringList("Races.List")) {
            raceList.add(race);
        }
        return raceList;
    }
}
