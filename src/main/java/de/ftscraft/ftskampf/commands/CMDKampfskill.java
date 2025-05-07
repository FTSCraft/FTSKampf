package de.ftscraft.ftskampf.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.db.DBManager;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Dice;
import de.ftscraft.ftskampf.utils.Message;
import de.ftscraft.ftskampf.utils.Race;
import de.ftscraft.ftskampf.utils.exceptions.NotEnoughPointsException;
import de.ftscraft.ftskampf.utils.exceptions.NumberNegativeException;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import de.ftscraft.ftskampf.utils.exceptions.SkillLimitException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CMDKampfskill implements CommandExecutor, TabCompleter {
    FTSKampf plugin = FTSKampf.getPlugin();
    Engine engine = plugin.getEngine();
    DBManager db = plugin.getDB();
    FileConfiguration config = plugin.getConfig();
    HpManager hpManager = plugin.getHpManager();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> argumentListe = Arrays.asList("nahkampf", "fernkampf", "agilität", "magie", "info", "help");
            List<String> result = new ArrayList<>();
            for (String arg : argumentListe) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(arg);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = ((Player) sender).getPlayer();
        } else {
            sender.sendMessage("Not a player");
            return true;
        }

        assert player != null;

        if (config.getBoolean("Permissions.SetSkills.Required")) {
            if (!(player.hasPermission(config.getString("Permissions.SetSkills.Name")))) {
                player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                return true;
            }
        }

        Ausweis ausweis = engine.getAusweis(player);
        if (ausweis == null || ausweis.getRace() == null) {
            player.sendMessage(Message.TAG + "§6Bitte lege dir zuerst einen Ausweis an (/ausweis). Für das Skillen ist folgendes wichtig: §cRasse");
            return true;
        }

        Race race;
        try {
            race = plugin.getRace(player);
        } catch (RaceDoNotExistException e) {
            player.sendMessage(Message.TAG + "§6Deine Rasse wurde nicht gefunden!");
            return true;
        }

        if (args.length < 1) {
            showHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            showHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            player.sendMessage(Message.TAG + "§7Du hast folgende Skills:");
            if (race.getSkill(Dice.MELEE) > 0) {
                player.sendMessage("§7Nahkampf: §c" + (db.getPlayerSkill(player).getMelee() + race.getMelee()) + " §7Skillpunkte");
            }
            if (race.getSkill(Dice.DISTANCE) > 0) {
                player.sendMessage("§7Fernkampf: §c" + (db.getPlayerSkill(player).getDistance() + race.getDistance()) + " §7Skillpunkte");
            }
            if (race.getSkill(Dice.MAGIC) > 0) {
                player.sendMessage("§7Magie: §c" + (db.getPlayerSkill(player).getMagic() + race.getMagic()) + " §7Skillpunkte");

            }
            if (race.getSkill(Dice.AGILITY) > 0) {
                player.sendMessage("§7Agilität: §c" + (db.getPlayerSkill(player).getAgility() + race.getAgility()) + " §7Skillpunkte");
            }
            player.sendMessage("§7Maximale Lebenspunkte: §c" + hpManager.getMaxHealth(player));

            player.sendMessage("§7Aktuell kannst du noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints vergeben");
            return true;
        }

        if (args[0].equalsIgnoreCase("reset") && args.length < 2) {
            if (config.getBoolean("Permissions.ResetSkills.Required")) {
                if (!(player.hasPermission(config.getString("Permissions.ResetSkills.Name")))) {
                    player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                    return true;
                }
            }
            player.sendMessage(Message.TAG + "§6Bitte bestätige die Aktion mit §c/kampfskill reset confirm");
            return true;
        }

        if (args[0].equalsIgnoreCase("remove") && args.length < 2) {
            if (config.getBoolean("Permissions.ResetOtherSkills.Required")) {
                if (!(player.hasPermission(config.getString("Permissions.ResetOtherSkills.Name")))) {
                    player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                    return true;
                }
            }
            player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill remove [Spielername]");
            return true;
        }

        if (args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("confirm")) {
            if (config.getBoolean("Permissions.ResetSkills.Required")) {
                if (!(player.hasPermission(config.getString("Permissions.ResetSkills.Name")))) {
                    player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                    return true;
                }
            }
            try {
                if (db.removeSkill(db.getPlayerSkill(player))) {
                    player.sendMessage(Message.TAG + "§6Deine Skills wurden zurückgesetzt!");
                } else {
                    player.sendMessage(Message.TAG + "§6Du hast noch keine Skills vergeben.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (config.getBoolean("Permissions.ResetOtherSkills.Required")) {
                if (!(player.hasPermission(config.getString("Permissions.ResetOtherSkills.Name")))) {
                    player.sendMessage(Message.TAG + "§6Du hast keine Berechtigung für diese Aktion!");
                    return true;
                }
            }
            String username = args[1];
            try {
                if (db.removeSkill(username)) {
                    player.sendMessage(Message.TAG + "§6Die Skills von §c" + username + " §6wurden zurückgesetzt!");
                } else {
                    player.sendMessage(Message.TAG + "§6Der Spieler §c" + username + " §6hatte keine Skills gesetzt");
                }
            } catch (Exception e) {
                player.sendMessage(Message.TAG + "§6Der Spieler §c" + username + " §6konnte nicht gefunden werden!");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("nahkampf")) {
            if (args.length < 2) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill nahkampf [Skillpunkte]");
                return true;
            }
            if (race.getSkill(Dice.MELEE) < 0) {
                player.sendMessage(Message.TAG + "§6Deine Rasse verfügt nicht über die Fähigkeit §c" + Dice.MELEE.getName() + "!");
                return true;
            }
            try {
                int skillpoints = Integer.parseInt(args[1]);
                db.addSkillpoints(skillpoints, player, Dice.MELEE);
            } catch (NumberFormatException e) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill nahkampf [Skillpunkte]");
            } catch (NotEnoughPointsException e) {
                player.sendMessage(Message.TAG + "§7Dafür reichen deine Punkte nicht aus! Du hast noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints");
            } catch (SkillLimitException e) {
                player.sendMessage(Message.TAG + "§7Du hast das maximale Limit für diesen Skill erreicht! Dir wurden §c" + e.getRemainingPoints() + " §7Skillpoints gutgeschrieben.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberNegativeException e) {
                player.sendMessage(Message.TAG + "§7Du musst mehr als 0 Skillpunkte angeben!");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("fernkampf")) {
            if (args.length < 2) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill fernkampf [Skillpunkte]");
                return true;
            }
            if (race.getSkill(Dice.DISTANCE) < 0) {
                player.sendMessage(Message.TAG + "§6Deine Rasse verfügt nicht über die Fähigkeit §c" + Dice.DISTANCE.getName() + "!");
                return true;
            }
            try {
                int skillpoints = Integer.parseInt(args[1]);
                db.addSkillpoints(skillpoints, player, Dice.DISTANCE);
            } catch (NumberFormatException e) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill fernkampf [Skillpunkte]");
            } catch (NotEnoughPointsException e) {
                player.sendMessage(Message.TAG + "§7Dafür reichen deine Punkte nicht aus! Du hast noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints");
            } catch (SkillLimitException e) {
                player.sendMessage(Message.TAG + "§7Du hast das maximale Limit für diesen Skill erreicht! Dir wurden §c" + e.getRemainingPoints() + " §7Skillpoints gutgeschrieben.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberNegativeException e) {
                player.sendMessage(Message.TAG + "§7Du musst mehr als 0 Skillpunkte angeben!");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("magie")) {
            if (args.length < 2) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill magie [Skillpunkte]");
                return true;
            }
            if (race.getSkill(Dice.MAGIC) < 0) {
                player.sendMessage(Message.TAG + "§6Deine Rasse verfügt nicht über die Fähigkeit §c" + Dice.MAGIC.getName() + "!");
                return true;
            }
            try {
                int skillpoints = Integer.parseInt(args[1]);
                db.addSkillpoints(skillpoints, player, Dice.MAGIC);
            } catch (NumberFormatException e) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill magie [Skillpunkte]");
            } catch (NotEnoughPointsException e) {
                player.sendMessage(Message.TAG + "§7Dafür reichen deine Punkte nicht aus! Du hast noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints");
            } catch (SkillLimitException e) {
                player.sendMessage(Message.TAG + "§7Du hast das maximale Limit für diesen Skill erreicht! Dir wurden §c" + e.getRemainingPoints() + " §7Skillpoints gutgeschrieben.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberNegativeException e) {
                player.sendMessage(Message.TAG + "§7Du musst mehr als 0 Skillpunkte angeben!");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("agilität")) {
            if (args.length < 2) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill agilität [Skillpunkte]");
                return true;
            }
            if (race.getSkill(Dice.AGILITY) < 0) {
                player.sendMessage(Message.TAG + "§6Deine Rasse verfügt nicht über die Fähigkeit §c" + Dice.AGILITY.getName() + "!");
                return true;
            }
            try {
                int skillpoints = Integer.parseInt(args[1]);
                db.addSkillpoints(skillpoints, player, Dice.AGILITY);
            } catch (NumberFormatException e) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill agilität [Skillpunkte]");
            } catch (NotEnoughPointsException e) {
                player.sendMessage(Message.TAG + "§7Dafür reichen deine Punkte nicht aus! Du hast noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints");
            } catch (SkillLimitException e) {
                player.sendMessage(Message.TAG + "§7Du hast das maximale Limit für diesen Skill erreicht! Dir wurden §c" + e.getRemainingPoints() + " §7Skillpoints gutgeschrieben.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberNegativeException e) {
                player.sendMessage(Message.TAG + "§7Du musst mehr als 0 Skillpunkte angeben!");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("hp")) {
            if (args.length < 2) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill hp [Skillpunkte]");
                return true;
            }
            try {
                int skillpoints = Integer.parseInt(args[1]);
                db.addHp(skillpoints, player);
            } catch (NumberFormatException e) {
                player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill hp [Skillpunkte]");
            } catch (NotEnoughPointsException e) {
                player.sendMessage(Message.TAG + "§7Dafür reichen deine Punkte nicht aus! Du hast noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberNegativeException e) {
                player.sendMessage(Message.TAG + "§7Du musst mehr als 0 Punkte angeben!");
            }
            return true;
        }

        player.sendMessage(Message.TAG + "§6Bitte benutze den Befehl so: §c/kampfskill [Skill] [Skillpunkte]");
        return true;
    }




    private void showHelp(Player player) {
        player.sendMessage(Message.TAG + "§c/kampfskill info: §6Zeigt dir deine aktuellen Skills an");

        if ((!config.getBoolean("Permissions.ResetSkills.Required") || player.hasPermission(config.getString("Permissions.ResetSkills.Name")))) {
            player.sendMessage("§c/kampfskill reset: §6Lässt dich all deine eignene Skillpunkte zurücksetzen");
        }

        if ((!config.getBoolean("Permissions.ResetOtherSkills.Required") || player.hasPermission(config.getString("Permissions.ResetOtherSkills.Name")))) {
            player.sendMessage("§c/kampfskill remove [Spielername]: §6Lässt dich die Skillpunkte eines Spielers zurücksetzen");
        }
        player.sendMessage("§c/kampfskill nahkampf [Skillpunkte]: §6Lässt dich Punkte für den Nahkampf verteilen");
        player.sendMessage("§c/kampfskill fernkampf [Skillpunkte]: §6Lässt dich Punkte für den Fernkampf verteilen");
        player.sendMessage("§c/kampfskill magie [Skillpunkte]: §6Lässt dich Punkte für Magie verteilen");
        player.sendMessage("§c/kampfskill agilität [Skillpunkte]: §6Lässt dich Agilitätspunkte (Beweglichkeit) verteilen");
        player.sendMessage("§c/kampfskill hp [Skillpunkte]: §6Lässt dich maximale Lebenspunkte verteilen");
        player.sendMessage("§7Jeder Skillpunkt in hp gibt dir §c" + config.getInt("Health.SkillMultiplier") + " §7maximale Lebenspunkte!");
        player.sendMessage("§7Aktuell kannst du noch §c" + db.getPlayerSkill(player).getPoints() + " §7Skillpoints vergeben");
    }
}
