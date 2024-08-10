package de.ftscraft.ftskampf.utils;

import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.configuration.file.FileConfiguration;

public class LivingEffectRunner implements Runnable {

    FTSKampf plugin = FTSKampf.getPlugin();
    EffectManager effectManager = plugin.getEffectManager();

    private int effectTimeIterator;

    public LivingEffectRunner() {
        effectTimeIterator = 0;
    }

    @Override
    public void run() {
        effectTimeIterator++;
        if (effectTimeIterator >= 60) {
            effectManager.iterateAllLivingEffects();
            effectTimeIterator = 0;
        }
    }

}
