package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.configuration.file.FileConfiguration;

public class HealOffsetRunner implements Runnable{

    FTSKampf plugin = FTSKampf.getPlugin();
    FileConfiguration config = plugin.getConfig();
    HpManager hpManager = plugin.getHpManager();
    private int iterator;

    public HealOffsetRunner() {
        iterator = 0;
    }

    @Override
    public void run() {
        iterator++;
        if(iterator >= 60) {
            hpManager.triggerOffsetMinute();
            iterator = 0;
        }
    }
}
