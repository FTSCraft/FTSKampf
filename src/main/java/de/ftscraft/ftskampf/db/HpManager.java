package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.main.Logger;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HpManager {

    private final List<Player> activePlayers = new ArrayList<>();
    private final FTSKampf plugin = FTSKampf.getPlugin();
    private final DBManager db = plugin.getDB();
    private final HashMap<String, Integer> health;
    private HashMap<String, Integer> offset;
    private final int OFFSET_MINUTES = plugin.getConfig().getInt("Health.HealtimeOffsetMinutes");

    {
        try {
            health = loadHealth();
            offset = loadOffset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerActivePlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!activePlayers.contains(uuid) && health.containsKey(uuid)) {
            activePlayers.add(player);
            Logger.log(player, "Marked as active");
        }
    }

    public void unregisterActivePlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if(activePlayers.contains(uuid)) {
            activePlayers.remove(uuid);
            Logger.log(player, "Marked as inactive");
        }
    }

    public void registerPlayer (Player player) {
        String uuid = player.getUniqueId().toString();
        if (!health.containsKey(uuid)) {
            health.put(uuid, 1);
        }
        registerActivePlayer(player);
        saveHp();
        Logger.log(player, "Registered in health list");
    }

    public void unregisterPlayer (Player player) {
        String uuid = player.getUniqueId().toString();
        health.remove(uuid);
        unregisterActivePlayer(player);
        saveHp();
        Logger.log(player, "Unregistered in health list");
    }

    public void healPlayer(Player player, int hpToHeal) {
        String uuid = player.getUniqueId().toString();
        if(!health.containsKey(uuid)) {
            return;
        }
        int oldHealth = health.get(uuid);
        int newHealth = oldHealth + hpToHeal;
        int maxHealth = plugin.getRaceOrDefault(player).getHealth();
        maxHealth += db.getPlayerSkill(player).getMaxHp();
        if(newHealth >= maxHealth) {
            unregisterPlayer(player);
            return;
        }
        health.put(uuid, newHealth);
        saveHp();
        Logger.log(player, "Healed from " + oldHealth + " to " + newHealth);
    }

    public void healAllPlayers(int hpTpHeal) {
        Logger.log("Triggered heal for all players");
        for(Player player : activePlayers) {
            if(!offset.containsKey(player.getUniqueId().toString())) {
                healPlayer(player, hpTpHeal);
            }
        }
    }

    public boolean isMaxHealthReached(Player player) {
        return getHealth(player) >= getMaxHealth(player);
    }

    public void hurtPlayer(Player player, int hpToHurt) {
        String uuid = player.getUniqueId().toString();
        int currentHp = getHealth(player);
        int newHealth = currentHp - hpToHurt;
        if(newHealth < 0) {
            newHealth = 0;
        }
        if(!health.containsKey(uuid)) {
            registerPlayer(player);
        }
        health.put(uuid, newHealth);
        Logger.log(player, "Hurt from " + currentHp + " to " + newHealth + " HP");
        addOffset(player);
        saveHp();
    }

    public boolean isPlayerHurt(Player player) {
        String uuid = player.getUniqueId().toString();
        return health.containsKey(uuid);
    }

    public int getHealth(Player player) {
        String uuid = player.getUniqueId().toString();
        int maxHealth = getMaxHealth(player);
        int currentHp;
        currentHp = health.getOrDefault(uuid, maxHealth);
        return currentHp;
    }

    public int getMaxHealth(Player player) {
        int maxHealth = plugin.getRaceOrDefault(player).getHealth();
        maxHealth += db.getPlayerSkill(player).getMaxHp();
        return maxHealth;
    }

    public void triggerOffsetMinute() {
        HashMap<String, Integer> newOffset = new HashMap<>();
        for(String player : offset.keySet()) {
            int mins = offset.get(player) + 1;
            if(mins < OFFSET_MINUTES) {
                newOffset.put(player, mins);
            }
            Logger.log(player, "Offset minute trriggered, raised to " + mins);
        }
        offset = newOffset;
        saveOffset();
    }

    public void addOffset(Player player) {
        String uuid = player.getUniqueId().toString();
        offset.remove(uuid);
        offset.put(uuid, 0);
        Logger.log(player, "Added to offset");
        saveOffset();
    }

    public int isPlayerInOffset(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!offset.containsKey(uuid))
            return -1;
        return OFFSET_MINUTES - offset.get(uuid);
    }

    private String getHealthPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/saves/healthpoints.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    private String getOffsetPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/saves/healthoffset.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    private HashMap<String, Integer> loadHealth() throws IOException {
        HashMap<String, Integer> health = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(getHealthPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            String uuid = values[0];
            int healthPoints = Integer.parseInt(values[1]);
            health.put(uuid, healthPoints);
        }
        reader.close();
        return health;
    }

    private HashMap<String, Integer> loadOffset() throws IOException {
        HashMap<String, Integer> healthOffset = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(getOffsetPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            String uuid = values[0];
            int minutesElapsed = Integer.parseInt(values[1]);
            healthOffset.put(uuid, minutesElapsed);
        }
        reader.close();
        return healthOffset;
    }

    private void saveHp() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getHealthPath()));
            for (String uuid : health.keySet()) {
                writer.write(uuid + ";" + health.get(uuid));
                writer.newLine();
            }
            writer.close();
        }
        catch (Exception e) {
            saveHp();
        }
    }

    private void saveOffset() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getOffsetPath()));
            for (String uuid : offset.keySet()) {
                writer.write(uuid + ";" + offset.get(uuid));
                writer.newLine();
            }
            writer.close();
        }
        catch (Exception e) {
            saveOffset();
        }
    }

    public void reset() {
        health.clear();
        offset.clear();
        saveHp();
        saveOffset();
    }
}
