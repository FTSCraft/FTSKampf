package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.main.FTSKampf;
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

    {
        try {
            health = loadHealth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerActivePlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (!activePlayers.contains(uuid) && health.containsKey(uuid)) {
            activePlayers.add(player);
        }
    }

    public void unregisterActivePlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (activePlayers.contains(uuid)) {
            activePlayers.remove(uuid);
        }
    }

    public void registerPlayer (Player player) {
        String uuid = player.getUniqueId().toString();
        if (!health.containsKey(uuid)) {
            health.put(uuid, 1);
        }
        registerActivePlayer(player);
        saveHp();
    }

    public void unregisterPlayer (Player player) {
        String uuid = player.getUniqueId().toString();
        if (health.containsKey(uuid)) {
            health.remove(uuid);
        }
        unregisterActivePlayer(player);
        saveHp();
    }

    public void healPlayer(Player player, int hpToHeal) {
        String uuid = player.getUniqueId().toString();
        if(!health.containsKey(uuid)) {
            return;
        }
        int newHealth = health.get(uuid);
        newHealth += hpToHeal;
        int maxHealth = plugin.getRaceOrDefault(player).getHealth();
        maxHealth += db.getPlayerSkill(player).getMaxHp();
        if(newHealth >= maxHealth) {
            unregisterPlayer(player);
            return;
        }
        health.put(uuid, newHealth);
        saveHp();
    }

    public void healAllPlayers(int hpTpHeal) {
        for(Player player : activePlayers) {
            healPlayer(player, hpTpHeal);
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
        if(!health.containsKey(uuid)) {
            currentHp = maxHealth;
        } else {
            currentHp = health.get(uuid);
        }
        return currentHp;
    }

    public int getMaxHealth(Player player) {
        int maxHealth = plugin.getRaceOrDefault(player).getHealth();
        maxHealth += db.getPlayerSkill(player).getMaxHp();
        return maxHealth;
    }

    private String getPath() throws IOException {
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

    private HashMap<String, Integer> loadHealth() throws IOException {
        HashMap<String, Integer> health = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(getPath()));
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

    private void saveHp() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getPath()));
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

    public void reset() {
        health.clear();
        saveHp();
    }
}
