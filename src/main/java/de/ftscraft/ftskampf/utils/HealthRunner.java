package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.configuration.file.FileConfiguration;

public class HealthRunner implements Runnable{

    FTSKampf plugin = FTSKampf.getPlugin();
    FileConfiguration config = plugin.getConfig();
    HpManager hpManager = plugin.getHpManager();
    private final int HEAL_PERIOD = config.getInt("Health.RegenRate");
    private final int HEAL_POINTS = config.getInt("Health.RegenPoints");
    private int healthIterator;

    public HealthRunner() {
        healthIterator = 0;
    }

    @Override
    public void run() {
        healthIterator++;
        if(healthIterator >= HEAL_PERIOD) {
            hpManager.healAllPlayers(HEAL_POINTS);
            healthIterator = 0;
        }
    }
}
