package de.ftscraft.ftskampf.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Logger {

    private static BufferedWriter writer;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        initializeLogFile();
    }

    private static void initializeLogFile() {
        try {
            FTSKampf plugin = FTSKampf.getPlugin();
            String fileTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String path = System.getProperty("user.dir");
            File logDirectory = new File(path + "/plugins/" + plugin.getName() + "/logs/");
            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }
            File logFile = new File(path + "/plugins/" + plugin.getName() + "/logs/" + "FTSKampf-log-" + fileTimestamp + ".txt");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(logFile, true));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void log(String playerName, String uuid, String message) {
        if(writer == null) {
            initializeLogFile();
        }
        String currentTimestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String logLine = currentTimestamp + " - " + playerName + " (" + uuid + "): " + message;
        try {
            writer.write(logLine);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        log("","No Player", message);
    }

    public static void log(Player player, String message) {
        if (player == null) {
            throw new IllegalArgumentException("Player darf nicht null sein!");
        }
        String playerName = player.getName();
        String uuid = player.getUniqueId().toString();
        log(playerName, uuid, message);
    }

    public static void log(String uuid, String message) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("UUID darf nicht null oder leer sein!");
        }
        String playerName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
        log(playerName, uuid, message);
    }

    public static void closeLogFile() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeLogFile();
    }
}

