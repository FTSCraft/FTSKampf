package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffectId;
import de.ftscraft.ftskampf.utils.LivingEffect;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffectManager {

    private final FTSKampf plugin = FTSKampf.getPlugin();
    private List<LivingEffect> activeEffects;

    public void init() {
        try {
            activeEffects = loadEffects();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    FileConfiguration config = plugin.getConfig();

    public void addEffect(ContinuousEffect effect) {
        removeEffect(effect);
        activeEffects.add(new LivingEffect(effect, 0));
        saveEffects();
    }

    public void addEffect(List<ContinuousEffect> effects) {
        for(ContinuousEffect effect : effects)
            addEffect(effect);
    }

    public void removeEffect(ContinuousEffect effect) {
        removeEffect(effect.getTarget(), effect.getCid());
    }

    public void removeEffect(List<LivingEffect> effects) {
        for (LivingEffect effect : effects)
            removeEffect(effect.getEffect().getTarget(), effect.getEffect().getCid());
    }

    public void removeEffect(String target, ContinuousEffectId cid) {
        List<LivingEffect> effectsToRemove = new ArrayList<>();
        for (LivingEffect effect : activeEffects) {
            ContinuousEffect cEffect = effect.getEffect();
            if (cEffect.getTarget().equalsIgnoreCase(target) && cEffect.getCid().getId().equalsIgnoreCase(cid.getId())) {
                effectsToRemove.add(effect);
            }
        }
        for(LivingEffect effect : effectsToRemove)
            activeEffects.remove(effect);
        saveEffects();
    }

    public List<ContinuousEffect> getPlayerEffects(String player) {
        List<ContinuousEffect> playerEffects = new ArrayList<>();
        for (LivingEffect effect : activeEffects) {
            if (effect.getEffect().getTarget().equalsIgnoreCase(player))
                playerEffects.add(effect.getEffect());
        }
        return playerEffects;
    }

    public List<ContinuousEffect> getPlayerCastetEffects(String player) {
        List<ContinuousEffect> playerEffects = new ArrayList<>();
        for (LivingEffect effect : activeEffects) {
            if (effect.getEffect().getCaster().equalsIgnoreCase(player))
                playerEffects.add(effect.getEffect());
        }
        return playerEffects;
    }

    public void iterateAllLivingEffects() {
        int effectMaxLifetime = config.getInt("EffectLifetime");
        List<LivingEffect> effectsToRemove = new ArrayList<>();
        for (LivingEffect effect : activeEffects) {
            effect.iterateLifetime();
            saveEffects();
            if (effect.getLifetime() >= effectMaxLifetime) {
                effectsToRemove.add(effect);
            }
        }
        removeEffect(effectsToRemove);
    }

    private List<LivingEffect> loadEffects() throws IOException {
        List<LivingEffect> effects = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(getPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            effects.add(new LivingEffect(ContinuousEffectFactory.createEffect(values), Integer.parseInt(values[5])));
        }
        reader.close();
        return effects;
    }

    private void saveEffects() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getPath()));
            for (LivingEffect lEffect : activeEffects) {
                ContinuousEffect effect = lEffect.getEffect();
                writer.write(effect.getCid().getId() + ";" + effect.getTarget() + ";" + effect.getCaster() + ";" + effect.getModifier() + ";" + effect.getDurability() + ";" + lEffect.getLifetime());
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            saveEffects();
        }
    }

    private String getPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/saves/effects.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    public void reset() {
        activeEffects.clear();
        saveEffects();
    }

}
