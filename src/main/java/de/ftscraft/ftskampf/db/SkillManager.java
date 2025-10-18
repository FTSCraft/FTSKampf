package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Skill;
import de.ftscraft.ftskampf.utils.exceptions.NotEnoughPointsException;
import de.ftscraft.ftskampf.utils.exceptions.NumberNegativeException;
import de.ftscraft.ftskampf.utils.exceptions.SkillLimitException;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SkillManager {
    FTSKampf plugin = FTSKampf.getPlugin();
    FileConfiguration config = plugin.getConfig();
    private final List<Skill> skills;
    private final HashMap<String, Integer> maxPoints;

    {
        try {
            skills = loadSkills();
            maxPoints = loadMaxPoints();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/saves/saves.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    private String getMaxPointsPath() throws IOException {
        String path = System.getProperty("user.dir");
        File saveDirectory = new File(path + "/plugins/" + plugin.getName() + "/saves/");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
        File file = new File(path + "/plugins/" + plugin.getName() + "/saves/maxPoints.csv");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    public List<Skill> loadSkills() throws IOException {
        List<Skill> skills = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(getPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            String uuid = values[0];
            int points = Integer.parseInt(values[1]);
            int melee = Integer.parseInt(values[2]);
            int distance = Integer.parseInt(values[3]);
            int magic = Integer.parseInt(values[4]);
            int agility = Integer.parseInt(values[5]);
            int maxHp = Integer.parseInt(values[6]);
            skills.add(new Skill(uuid, points, melee, distance, magic, agility, maxHp));
        }
        reader.close();
        return skills;
    }

    public HashMap<String, Integer> loadMaxPoints() throws IOException {
        HashMap<String, Integer> maxPoints = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(getMaxPointsPath()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            String uuid = values[0];
            int points = Integer.parseInt(values[1]);
            maxPoints.put(uuid, points);
        }
        reader.close();
        return maxPoints;
    }

    public void addSkillpoints(int points, Player player, Dice dice) throws NotEnoughPointsException, SkillLimitException, IOException, NumberNegativeException {
        if(points < 1) {
            throw new NumberNegativeException();
        }
        Skill skill = getPlayerSkill(player);
        int skillpoints = skill.getSkill(dice);
        if (points > skill.getPoints()) {
            throw new NotEnoughPointsException("Not enough points!");
        }
        int remainingPoints = skill.getPoints() - points;
        skillpoints += points;
        int racePoints = plugin.getRaceOrDefault(player).getSkill(dice);
        int finalSkillpoints = racePoints + skillpoints;
        if (finalSkillpoints > dice.getMaxSkillable()) {
            skill.setSkill(dice, dice.getMaxSkillable()-racePoints);
            int overflow = finalSkillpoints - dice.getMaxSkillable();
            remainingPoints += overflow;
            skill.setPoints(remainingPoints);
            addSkill(skill);
            if(overflow < dice.getMaxSkillable()) {
                player.sendMessage("§6Dein Skill für §c" + dice.getName() + " §6hat nun §c" + dice.getMaxSkillable() + " §6Punkte!");
                player.sendMessage("§6Aktuell kannst du noch §c" + remainingPoints + " §6Skillpoints vergeben");
            }
            throw new SkillLimitException("Skill limit reached", overflow);
        }
        skill.setSkill(dice, skillpoints);
        skill.setPoints(remainingPoints);
        player.sendMessage("§6Dein Skill für §c" + dice.getName() + " §6hat nun §c" + finalSkillpoints  + " §6Punkte!" );
        player.sendMessage("§6Aktuell kannst du noch §c" + remainingPoints + " §6Skillpoints vergeben");
        addSkill(skill);
    }

    public void addHp(int points, Player player) throws NotEnoughPointsException, IOException, NumberNegativeException {
        if(points < 1) {
            throw new NumberNegativeException();
        }
        Skill skill = getPlayerSkill(player);
        int skillpoints = skill.getMaxHp();
        if (points > skill.getPoints()) {
            throw new NotEnoughPointsException("Not enough points!");
        }
        int remainingPoints = skill.getPoints() - points;
        skillpoints += points * config.getInt("Health.SkillMultiplier");
        int racePoints = plugin.getRaceOrDefault(player).getHealth();
        int finalSkillpoints = racePoints + skillpoints;
        skill.setMaxHp(skillpoints);
        skill.setPoints(remainingPoints);
        player.sendMessage("§6Deine maximalen Lebenspunkte sind nun §c" + finalSkillpoints  + " §6Punkte!" );
        player.sendMessage("§6Aktuell kannst du noch §c" + remainingPoints + " §6Skillpoints vergeben");
        addSkill(skill);
    }

    public boolean removeSkill(Skill skill) throws IOException {
        Iterator<Skill> iterator = skills.iterator();
        while (iterator.hasNext()) {
            Skill skillSearch = iterator.next();
            if (skillSearch.getUUID().equals(skill.getUUID())) {
                iterator.remove();
                saveSkills();
                return true;
            }
        }
        return false;
    }

    public boolean removeSkill(String username) throws Exception {
        String uuid = getUUID(username);
        Iterator<Skill> iterator = skills.iterator();
        while (iterator.hasNext()) {
            Skill skillSearch = iterator.next();
            if (skillSearch.getUUID().equals(uuid)) {
                iterator.remove();
                saveSkills();
                return true;
            }
        }
        return false;
    }

    public void setMaxSkillpoints(String username, int points) throws Exception {
        if(points < 0) throw new NumberNegativeException();
        String uuid = getUUID(username);
        maxPoints.remove(uuid);
        maxPoints.put(uuid, points);
        saveMaxPoints();
        removeSkill(username);
    }

    public void resetMaxSkillpoints(String username) throws Exception {
        String uuid = getUUID(username);
        maxPoints.remove(uuid);
        saveMaxPoints();
        removeSkill(username);
    }

    public String getUUID(String username) throws Exception {
        String urlString = "https://api.mojang.com/users/profiles/minecraft/" + username;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.connect();
        String responseString = IOUtils.toString(connection.getInputStream(), "UTF-8");
        int index = responseString.indexOf("\"id\"");
        if (index < 0) {
            throw new Exception("Invalid response from Mojang API: " + responseString);
        }
        String uuid = responseString.substring(index + 8, index + 8 + 32);
        if(uuid.contains("\"")) {
            uuid = responseString.substring(index + 6, index + 6 + 32);
        }
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
    }

    private void addSkill(Skill newSkill) throws IOException {
        removeSkill(newSkill);
        skills.add(newSkill);
        saveSkills();
    }

    private void saveSkills() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(getPath()));
        for (Skill skill : skills) {
            String uuid = skill.getUUID();
            int points = skill.getPoints();
            int melee = skill.getMelee();
            int distance = skill.getDistance();
            int magic = skill.getMagic();
            int agility = skill.getAgility();
            int maxHp = skill.getMaxHp();
            writer.write(uuid + ";" + points + ";" + melee + ";" + distance + ";" + magic + ";" + agility + ";" + maxHp);
            writer.newLine();
        }
        writer.close();
    }

    private void saveMaxPoints() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(getMaxPointsPath()));
        for (String key : maxPoints.keySet()) {
            String uuid = key;
            int points = maxPoints.get(uuid);
            writer.write(uuid + ";" + points);
            writer.newLine();
        }
        writer.close();
    }

    public Skill getPlayerSkill(Player player) {
        String uuid = player.getUniqueId().toString();
        for (Skill skill : skills) {
            if (skill.getUUID().equals(uuid)) {
                return skill;
            }
        }
        int points = plugin.getRaceOrDefault(player).getPoints();
        if(maxPoints.containsKey(uuid)) points = maxPoints.get(uuid);
        return new Skill(uuid, points);
    }

    public void reset() {
        skills.clear();
        maxPoints.clear();
        try {
            saveSkills();
            saveMaxPoints();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
