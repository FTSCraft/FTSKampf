package de.ftscraft.ftskampf.damageCalculators;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftskampf.db.SkillManager;
import de.ftscraft.ftskampf.db.EffectManager;
import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.main.Logger;
import de.ftscraft.ftskampf.spells.effects.effectDefinitions.ContinuousEffect;
import de.ftscraft.ftskampf.utils.*;
import de.ftscraft.ftskampf.utils.exceptions.NumberNegativeException;
import de.ftscraft.ftskampf.utils.exceptions.RaceDoNotExistException;
import de.ftscraft.ftssystem.main.FtsSystem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DiceManager {
    FTSKampf plugin = FTSKampf.getPlugin();
    private HashMap<Player, Attack> attacks = new HashMap<>();
    Engine engine = plugin.getEngine();
    SkillManager skillManager = plugin.getSkillManager();
    HpManager hpManager = plugin.getHpManager();
    DamageModifier damageModifier = new DamageModifier();
    FileConfiguration config = plugin.getConfig();
    EffectManager effectManager = plugin.getEffectManager();

    public void registerAttack(Player target, Attack attack) {
        attacks.remove(target);
        attacks.put(target, attack);
    }

    public boolean isAttacked(Player player) {
        return attacks.containsKey(player);
    }

    public void removeAttack(Player player) {
        attacks.remove(player);
    }

    private Attack getAttack(Player target) {
        if (attacks.containsKey(target)) {
            return attacks.get(target);
        }
        return null;
    }

    public PlainResult rollPlainDice(Dice dice, Player player) {
        Race race = plugin.getRaceOrDefault(player);
        int value = calculateAttackValue(dice, player);
        int skill = calculateSkill(player, dice, race);
        boolean success = value <= skill;
        return new PlainResult(value, skill, success);
    }

    public void rollDice(Dice dice, Player player) throws RaceDoNotExistException {
        Race race = plugin.getRace(player);
        Ausweis ausweis = engine.getAusweis(player);
        if (race.getSkill(dice) < 0) {
            player.sendMessage(Message.TAG + "§6Die Rasse §o" + race.getmName() + " §6verfügt nicht über die Fähigkeit §o" + dice.getName() + "!");
            return;
        }

        Ausweis.Gender gender = ausweis.getGender();
        String article = "Der";
        String raceName = race.getmName();
        if (gender.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }

        doDamageOverTime(player);

        String name = "§7" + article + " §o" + raceName + " §r§e" + getName(player);

        StringBuilder message = new StringBuilder(name + " §7würfelt: §e");

        int value = calculateAttackValue(dice, player);
        int skill = calculateSkill(player, dice, race);

        boolean success = value <= skill;
        if (success) {
            message.append("§2").append(value).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(dice.getName()).append("]");
        } else {
            message.append("§c").append(value).append(" §7hätte aber §c").append(skill).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(dice.getName()).append("]");
        }
        sendMessageInRange(message, player);
        if (success && !dice.equals(Dice.AGILITY))
            sendMessageInRange(Message.TAG + name + " §7greift an mit der Stärke von §c" + calculateAttackStrength(player, dice, value) + " §5[" + dice.getName() + "]", player);
    }

    public void rollActionDice(Dice dice, Player player) throws RaceDoNotExistException {
        Race race = plugin.getRace(player);
        Ausweis ausweis = engine.getAusweis(player);

        Ausweis.Gender gender = ausweis.getGender();
        String article = "Der";
        String raceName = race.getmName();
        if (gender.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }

        int value = calculateAttackValue(dice, player);

        int skill = dice.equals(Dice.ACTION) ? 51 : skillManager.getPlayerSkill(player).getSkill(dice) + race.getSkill(dice);

        String colourCode = value < skill ? "§2" : "§c";

        StringBuilder message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + getName(player) + " §7würfelt: §e").append(colourCode).append(value).append(" §5[").append(dice.getName()).append("]");
        sendMessageInRange(message, player);
    }

    public PlainResult rollMagicPreDice(Player player) {
        return rollPlainDice(Dice.MAGIC, player);
    }

    public void rollTargetDice(Dice dice, Player player, Player target) throws RaceDoNotExistException {
        rollTargetDice(dice, player, target, 1, 0, false);
    }

    public void rollTargetDice(Dice dice, Player player, Player target, double modifier) throws RaceDoNotExistException {
        rollTargetDice(dice, player, target, modifier, 0, false);
    }

    public void rollTargetDice(Dice dice, Player player, Player target, double modifier, double absorptionRate) throws RaceDoNotExistException {
        rollTargetDice(dice, player, target, modifier, absorptionRate, false);
    }

    public void rollTargetDice(Dice dice, Player player, Player target, double modifier, boolean penetrateArmor) throws RaceDoNotExistException {
        rollTargetDice(dice, player, target, modifier, 0, penetrateArmor);
    }

    public void rollTargetDice(Dice dice, Player player, Player target, double modifier, double absorptionRate, boolean penetrateArmor) throws RaceDoNotExistException {
        Race race = plugin.getRace(player);
        Ausweis ausweis = engine.getAusweis(player);
        if (race.getSkill(dice) < 0) {
            player.sendMessage(Message.TAG + "§6Die Rasse §o" + race.getmName() + " §6verfügt nicht über die Fähigkeit §o" + dice.getName() + "!");
            return;
        }

        Race targetRace = plugin.getRaceOrDefault(target);
        if (targetRace.getSkill(dice) < 0) {
            player.sendMessage(Message.TAG + "§6Die Rasse §o" + targetRace.getmName() + " §6von §c" + target.getName() + " §6kann nicht mit §o" + dice.getName() + " §6angegriffen werden!");
            return;
        }

        doDamageOverTime(player);

        Ausweis.Gender gender = ausweis.getGender();
        if (gender == null) {
            gender = Ausweis.Gender.MALE;
        }
        String article = "Der";
        String raceName = race.getmName();
        if (gender.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }

        String articleTarget = "den";
        String raceNameTarget = targetRace.getmName();
        String targetName = target.getName();
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(target);
            Ausweis.Gender tarGender = targetAusweis.getGender();
            if (tarGender == null) {
                tarGender = Ausweis.Gender.MALE;
            }
            if (tarGender.equals(Ausweis.Gender.FEMALE)) {
                articleTarget = "die";
                raceNameTarget = targetRace.getfName();
            }
            targetName = targetAusweis.getFirstName() + " " + targetAusweis.getLastName();
        }

        StringBuilder message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + getName(player) + " §7greift §7" + articleTarget + " §o" + raceNameTarget + " §e" + targetName + " §7an und würfelt: §e");

        int value = calculateAttackValue(dice, player);
        int skill = calculateSkill(player, dice, race);

        boolean success = value <= skill;
        if (success) {
            value = Math.min(value, 100);
            message.append("§2").append(value).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(dice.getName()).append("]");
        } else {
            message.append("§c").append(value).append(" §7hätte aber §c").append(skill).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(dice.getName()).append("]");
        }
        sendMessageInRange(message, player, target);
        Logger.log(player, "Tries to attack with target dice: " + target.getName() + " ," + dice.getName());
        if (success) {
            if (isProtected(target)) {
                sendMessageInRange(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + getName(player) + " §7greift §7" + articleTarget + " §o" + raceNameTarget + " §e" + targetName + " §7an, doch §e" + targetName + " §7ist zur Zeit immun", player, target);
                return;
            }
            int attackStrength = calculateAttackStrength(player, dice, value, modifier);
            sendMessageInRange(Message.TAG + getName(player) + " §7greift an mit der Stärke von §c" + attackStrength + " §5[" + dice.getName() + "]", player, target);
            TextComponent message1 = new TextComponent(Message.TAG + "§7Du wirst angegriffen! Klicke hier um ");
            TextComponent reaction1 = new TextComponent("§e[Auszuweichen]");
            reaction1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/react dodge"));
            reaction1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ausweichen").create()));
            TextComponent message2 = new TextComponent(" §7,zu ");
            TextComponent reaction2 = new TextComponent("§c[Kontern]");
            reaction2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/react counter"));
            reaction2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Kontern").create()));
            TextComponent message3 = new TextComponent(" §7oder ");
            TextComponent reaction3 = new TextComponent("§a[Anzunehmen]");
            reaction2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/react accept"));
            reaction2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Annehmen").create()));
            TextComponent message4 = new TextComponent("§7!" + " §5[" + dice.getName() + "]");
            TextComponent text = message1;
            text.addExtra(reaction1);
            text.addExtra(message2);
            text.addExtra(reaction2);
            text.addExtra(message3);
            text.addExtra(reaction3);
            text.addExtra(message4);
            target.sendMessage(text);
            registerAttack(target, new Attack(player, dice, attackStrength, absorptionRate, penetrateArmor));
            Logger.log(player, "Attacks with target dice: " + target.getName() + ", " + dice.getName() + ", " + value);
        }
    }

    public void rollHealDice(Dice dice, Player player, Player target) throws RaceDoNotExistException {
        rollHealDice(dice, player, target, 1);
    }

    public void rollHealDice(Dice dice, Player player, Player target, double modifier) throws RaceDoNotExistException {
        Race race = plugin.getRace(player);
        Ausweis ausweis = engine.getAusweis(player);
        if (race.getSkill(dice) < 0) {
            player.sendMessage(Message.TAG + "§6Die Rasse §o" + race.getmName() + " §6verfügt nicht über die Fähigkeit §o" + dice.getName() + "!");
            return;
        }

        Race targetRace = plugin.getRaceOrDefault(target);
        if (targetRace.getSkill(dice) < 0) {
            player.sendMessage(Message.TAG + "§6Die Rasse §o" + targetRace.getmName() + " §6von §c" + target.getName() + " §6kann nicht mit §o" + dice.getName() + " §6geheilt werden!");
            return;
        }

        if (!hpManager.isPlayerHurt(target)) {
            sendMessageInRange(Message.TAG + "§c" + target.getName() + " §6ist nicht verletzt!", player);
            return;
        }

        Ausweis.Gender gender = ausweis.getGender();
        String article = "Der";
        String raceName = race.getmName();
        if (gender.equals(Ausweis.Gender.FEMALE)) {
            article = "Die";
            raceName = race.getfName();
        }

        String articleTarget = "den";
        String raceNameTarget = targetRace.getmName();
        String targetName = getName(target);
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(player);
            Ausweis.Gender tarGender = targetAusweis.getGender();
            if (tarGender == null) {
                tarGender = Ausweis.Gender.MALE;
            }
            if (tarGender.equals(Ausweis.Gender.FEMALE)) {
                articleTarget = "die";
                raceNameTarget = race.getfName();
            }
        }

        StringBuilder message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + getName(player) + " §7versucht §7" + articleTarget + " §o" + raceNameTarget + " §e" + targetName + " §7zu heilen und würfelt: §e");

        int value = calculateAttackValue(dice, player);
        int skill = calculateSkill(player, dice, race);

        boolean success = value <= skill;
        int finalValue = calculateAttackStrength(player, dice, value);
        if (success) {
            message.append("§2").append(value).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(dice.getName()).append("]");
            try {
                hpManager.healPlayer(target, (int) Math.round(modifier * finalValue));
            } catch (NumberNegativeException ignored) {
            }
        } else {
            message.append("§c").append(value).append(" §7hätte aber §c").append(skill).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(dice.getName()).append("]");
        }
        sendMessageInRange(message, player, target);
        Logger.log(player, "Tries to heal " + target.getName());
        if (success) {
            sendMessageInRange(Message.TAG + "§7Geheilt werden §c" + (int) Math.round(modifier * finalValue) + " §7LP!", player, target);
            if (hpManager.isMaxHealthReached(target)) {
                target.sendMessage(Message.TAG + "§7Du bist nun vollständig geheilt!");
            } else {
                target.sendMessage(Message.TAG + "§7Du hast nun §c" + hpManager.getHealth(target) + " §7Lebenspunkte!");
            }
            Logger.log(player, "healed " + target.getName());
        }
    }

    public void rollDodge(Player target) {
        Attack attack = getAttack(target);
        if (attack == null) {
            return;
        }

        Race race = plugin.getRaceOrDefault(target);

        Race targetRace = plugin.getRaceOrDefault(target);
        String article = "Der";
        String raceName = targetRace.getmName();
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(target);
            Ausweis.Gender tarGender = targetAusweis.getGender();
            if (tarGender == null) {
                tarGender = Ausweis.Gender.MALE;
            }
            if (tarGender.equals(Ausweis.Gender.FEMALE)) {
                article = "Die";
                raceName = race.getfName();
            }
        }

        StringBuilder message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + getName(target) + " §7versucht auszuweichen und würfelt: §e");

        int value = calculateAttackValue(Dice.AGILITY, target);
        int skill = calculateSkill(target, Dice.AGILITY, race);
        skill = calculateAttackStrength(target, Dice.AGILITY, skill);
        skill = calculateAgilityWithArmor(target, skill);
        boolean success = value <= skill;
        if (success) {
            message.append("§2").append(value).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(Dice.AGILITY.getName()).append("]");
        } else {
            message.append("§c").append(value).append(" §7hätte aber §c").append(skill).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(Dice.AGILITY.getName()).append("]");
        }
        removeAttack(target);
        Player attacker = attack.getAttacker();
        sendMessageInRange(message, target, attacker);
        Logger.log(target, "dodges attack from " + attack.getAttacker().getName() + ", " + attack.getType().getName());
        if (!success) {
            int damage = calculateValueAfterDefend(target, attack.getStrength(), attack.getType(), attack.doPenetrateArmor());
            sendMessageInRange(Message.TAG + "§e" + getName(attack.getAttacker()) + " §7verursacht Schaden in Höhe von §c" + damage + " §7an §e" + getName(target) + " §5[" + attack.getType().getName() + "]", target, attacker);
            hpManager.hurtPlayer(target, damage);

            Logger.log(target, "Dodge failed, Damage: " + damage);

            if (attack.getAbsorptionRate() > 0) {
                int absorbedLife = (int) Math.round(damage * attack.getAbsorptionRate());
                Logger.log(target, absorbedLife + " HP got absorbed by " + attack.getAttacker().getName());
                try {
                    hpManager.healPlayer(attacker, absorbedLife);
                } catch (NumberNegativeException ignored) {
                }
                sendMessageInRange(Message.TAG + "§c" + getName(attacker) + " §7absorbiert §c" + absorbedLife + " §7Lebenspunkte", attacker, target);
            }

            if (hpManager.getHealth(target) <= 0) {
                sendMessageInRange(Message.TAG + "§c" + getName(target) + " §7ist kampfunfähig!", target, attacker);
            } else {
                sendMessageInRange(Message.TAG + "§c" + getName(target) + " §7hat noch §c" + hpManager.getHealth(target) + " §7HP!", target, attacker);
            }
        }
    }

    public void rollAccept(Player target) {
        Attack attack = getAttack(target);
        if (attack == null) {
            return;
        }

        Race race = plugin.getRaceOrDefault(target);

        Race targetRace = plugin.getRaceOrDefault(target);
        String article = "Der";
        String raceName = targetRace.getmName();
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(target);
            Ausweis.Gender tarGender = targetAusweis.getGender();
            if (tarGender == null) {
                tarGender = Ausweis.Gender.MALE;
            }
            if (tarGender.equals(Ausweis.Gender.FEMALE)) {
                article = "Die";
                raceName = race.getfName();
            }
        }

        StringBuilder message = new StringBuilder("§7" + article + " §o" + raceName + " §r§e" + getName(target) + " §7reagiert nicht auf den Angriff.§e");

        removeAttack(target);
        Player attacker = attack.getAttacker();

        sendMessageInRange(message, target, attacker);
        Logger.log(target, "Accepts attack from " + attack.getAttacker().getName() + ", " + attack.getType().getName());

        int damage = calculateValueAfterDefend(target, attack.getStrength(), attack.getType(), attack.doPenetrateArmor());
        sendMessageInRange(Message.TAG + "§e" + getName(attack.getAttacker()) + " §7verursacht Schaden in Höhe von §c" + damage + " §7an §e" + getName(target) + " §5[" + attack.getType().getName() + "]", target, attacker);
        hpManager.hurtPlayer(target, damage);

        if (attack.getAbsorptionRate() > 0) {
            int absorbedLife = (int) Math.round(damage * attack.getAbsorptionRate());
            Logger.log(target, absorbedLife + " HP got absorbed by " + attack.getAttacker().getName());
            try {
                hpManager.healPlayer(attacker, absorbedLife);
            } catch (NumberNegativeException ignored) {
            }
            sendMessageInRange(Message.TAG + "§c" + getName(attacker) + " §7absorbiert §c" + absorbedLife + " §7Lebenspunkte",attacker , target);
        }

        if (hpManager.getHealth(target) <= 0) {
            sendMessageInRange(Message.TAG + "§c" + getName(target) + " §7ist kampfunfähig!", target, attacker);
        } else {
            sendMessageInRange(Message.TAG + "§c" + getName(target) + " §7hat noch §c" + hpManager.getHealth(target) + " §7HP!", target, attacker);
        }
    }

    public void rollCounter(Player target) {
        Attack attack = getAttack(target);
        if (attack == null) {
            return;
        }

        Race race = plugin.getRaceOrDefault(target);

        String article = "Der";
        String raceName = race.getmName();
        if (engine.hasAusweis(target)) {
            Ausweis targetAusweis = engine.getAusweis(target);
            Ausweis.Gender tarGender = targetAusweis.getGender();
            if (tarGender == null) {
                tarGender = Ausweis.Gender.MALE;
            }
            if (tarGender.equals(Ausweis.Gender.FEMALE)) {
                article = "Die";
                raceName = race.getfName();
            }
        }

        StringBuilder message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + getName(target) + " §7versucht zu Kontern und würfelt: §e");

        Dice dice = attack.getType();
        int value = calculateAttackValue(dice, target);
        int skill = calculateSkill(target, dice, race);

        removeAttack(target);
        boolean success = value <= skill;
        int attackDmg;
        Logger.log(target, "tries to counter attack from " + attack.getAttacker().getName() + ", " + attack.getType().getName());
        if (success) {
            message.append("§2").append(value).append(" §7und hat damit den Wurf §2geschafft!").append(" §5[").append(dice.getName()).append("]");
            int counterDamage = calculateAttackStrength(target, dice, value);
            attackDmg = calculateDifference(attack.getStrength(), counterDamage);
            sendMessageInRange(message, target);
            message = new StringBuilder(Message.TAG + "§7" + article + " §o" + raceName + " §r§e" + getName(target) + " §7versucht zu Kontern mit: §c").append(counterDamage).append(" §5[").append(dice.getName()).append("]");
        } else {
            message.append("§c").append(value).append(" §7hätte aber §c").append(skill).append(" §7oder niedriger würfeln müssen!").append(" §5[").append(dice.getName()).append("]");
            attackDmg = attack.getStrength();
        }
        Player attacker = attack.getAttacker();
        sendMessageInRange(message, target, attacker);

        if (attackDmg < 0) {
            attacker = target;
            target = attack.getAttacker();
            attackDmg = Math.abs(attackDmg);
        }

        sendMessageInRange(Message.TAG + getName(attacker) + " §7greift an mit der Stärke von §c" + attackDmg + " §5[" + dice.getName() + "]", attacker, target);
        Logger.log(attacker, "Attack after counter: " + target.getName() + ", " + attack.getType().getName() + ", " + attackDmg);

        int damage = calculateValueAfterDefend(target, attackDmg, dice, attack.doPenetrateArmor());
        sendMessageInRange(Message.TAG + "§e" + getName(attacker) + " §7verursacht Schaden in Höhe von §c" + damage + " §7an §e" + getName(target) + " §5[" + attack.getType().getName() + "]", target, attacker);
        hpManager.hurtPlayer(target, damage);

        if (!success && attack.getAbsorptionRate() > 0) {
            int absorbedLife = (int) Math.round(damage * attack.getAbsorptionRate());
            try {
                hpManager.healPlayer(attacker, absorbedLife);
            } catch (NumberNegativeException ignored) {
            }
            sendMessageInRange(Message.TAG + "§c" + getName(attacker) + " §7absorbiert §c" + absorbedLife + " §7Lebenspunkte", attacker, target);
        }

        if (hpManager.getHealth(target) <= 0) {
            sendMessageInRange(Message.TAG + "§c" + getName(target) + " §7ist kampfunfähig!", target, attacker);
        } else {
            sendMessageInRange(Message.TAG + "§c" + getName(target) + " §7hat noch §c" + hpManager.getHealth(target) + " §7HP!", target, attacker);
        }
    }

    private int calculateAttackValue(Dice dice, Player player) {
        int initialValue = ThreadLocalRandom.current().nextInt(1, dice.getSize() + 1);
        return initialValue;
    }

    private int calculateSkill(Player player, Dice dice, Race race) {
        int skill = race.getSkill(dice);
        skill += skillManager.getPlayerSkill(player).getSkill(dice);
        ItemStack weapon = player.getInventory().getItemInMainHand();
        int skillNew = damageModifier.getModifiedHitchance(skill, dice, weapon);
        if (skill != skillNew) {
            String weaponName = "Axt";
            int reducedPercentage = config.getInt("Weapon.Melee.AxePrecisionLoss");
            if (weapon.getType().equals(Material.CROSSBOW)) {
                weaponName = "Armbrust";
                reducedPercentage = config.getInt("Weapon.Distance.CrossbowPrecisionLoss");
            }
            player.sendMessage(Message.TAG + "§7Die Verwendung einer " + weaponName + " verringert deine Trefferwahrscheinlichkeit um §c" + reducedPercentage + "%");
        }
        return skillNew;
    }

    private int calculateAttackStrength(Player player, Dice dice, int initialValue) {
        return calculateAttackStrength(player, dice, initialValue, 1);
    }

    private int calculateAttackStrength(Player player, Dice dice, int initialValue, double modifier) {
        initialValue = damageModifier.getModifiedAttack(initialValue, dice, player.getInventory().getItemInMainHand());
        for (ContinuousEffect effect : effectManager.getPlayerEffects(player.getUniqueId().toString())) {
            initialValue = effect.modifyAttackValue(initialValue, dice, !damageModifier.isArmed(player));
        }
        return (int) Math.round(initialValue * modifier);
    }

    private int calculateValueAfterDefend(Player player, int value, Dice dice, boolean armorPen) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        boolean hasShield = player.getInventory().getItemInMainHand().getType().equals(Material.SHIELD) || player.getInventory().getItemInOffHand().getType().equals(Material.SHIELD);
        int defendValue = damageModifier.getModifiedDefend(value, armor, hasShield, armorPen);

        for (ContinuousEffect effect : effectManager.getPlayerEffects(player.getUniqueId().toString())) {
            defendValue = effect.modifyDefendValue(defendValue, dice);
        }

        return Math.max(defendValue, plugin.getConfig().getInt("MinimumDamage"));
    }

    private int calculateAgilityWithArmor(Player player, int value) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        return damageModifier.getModifiedAgility(value, armor);
    }

    private int calculateDifference(int valueAttack, int valueDefend) {
        int difference = valueAttack - valueDefend;
        int minDamage = plugin.getConfig().getInt("MinimumDamage");
        if (Math.abs(difference) < minDamage) {
            if (difference < 0)
                return -minDamage;
            else
                return minDamage;
        }
        return difference;
    }

    public void sendMessageInRange(String message, Player player) {
        int range = getChatRange(player);
        for (Entity nearbyEntity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range)) {
            if (nearbyEntity instanceof Player) {
                nearbyEntity.sendMessage(message);
            }
        }
    }

    public void sendMessageInRange(String message, Player player, Player target) {
        List<Player> receiver = new ArrayList<>();
        receiver.add(player);
        receiver.add(target);
        sendMessageInMultipleRange(message, receiver);
    }

    public void sendMessageInRange(StringBuilder message, Player player, Player target) {
        List<Player> receiver = new ArrayList<>();
        receiver.add(player);
        receiver.add(target);
        sendMessageInMultipleRange(message.toString(), receiver);
    }


    public void sendMessageInMultipleRange(String message, List<Player> players) {
        List<Player> receivers = new ArrayList<>();
        for (Player player : players) {
            int range = getChatRange(player);
            for (Entity nearbyEntity : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), range, range, range)) {
                if (nearbyEntity instanceof Player) {
                    receivers.add((Player) nearbyEntity);
                }
            }
        }
        Set<Player> set = new HashSet<>(receivers);
        receivers.clear();
        receivers.addAll(set);
        for (Player receiver : receivers) {
            receiver.sendMessage(message);
        }
    }

    private void doDamageOverTime(Player player) {
        HashMap<String, Integer> targetMap = new HashMap<>();
        for (ContinuousEffect playerCastetEffect : effectManager.getPlayerCastetEffects(player.getUniqueId().toString())) {
            if (playerCastetEffect.returnDamage() > 0) {
                String target = playerCastetEffect.getTarget();
                int v = playerCastetEffect.returnDamage();
                if (targetMap.containsKey(target)) {
                    v += targetMap.get(target);
                    targetMap.remove(target);
                }
                targetMap.put(target, v);
            }
        }
        if (targetMap.isEmpty()) {
            return;
        }
        List<String> targets = new ArrayList<>();
        targets.addAll(targetMap.keySet());
        for (String target : targets) {
            int damage = targetMap.get(target);
            Player tarPlayer = Bukkit.getPlayer(UUID.fromString(target));
            if (tarPlayer == null) {
                return;
            }
            if (!tarPlayer.isOnline()) {
                return;
            }
            hpManager.hurtPlayer(tarPlayer, calculateValueAfterDefend(tarPlayer, damage, Dice.MAGIC, false));
            StringBuilder message = new StringBuilder(Message.TAG + "§e" + getName(player) + " §7verursacht Schaden in Höhe von §c" + damage + " §7 an §e" + getName(tarPlayer) + " §7(Schaden über Zeit) " + "§5[" + Dice.MAGIC.getName() + "]");
            List<Player> receivers = new ArrayList<>();
            receivers.add(player);
            receivers.add(tarPlayer);
            sendMessageInMultipleRange(message.toString(), receivers);
        }
    }

    public String getName(Player player) {
        if (!engine.hasAusweis(player)) {
            return player.getName();
        }
        Ausweis ausweis = engine.getAusweis(player);
        return ausweis.getFirstName() + " " + ausweis.getLastName();
    }

    public void sendMessageInRange(StringBuilder message, Player player) {
        sendMessageInRange(message.toString(), player);
    }

    private boolean isProtected(Player player) {
        for (ContinuousEffect effect : effectManager.getPlayerEffects(player.getUniqueId().toString())) {
            if (effect.isProtected()) {
                return true;
            }
        }
        return false;
    }

    private int getChatRange(Player player) {
        FtsSystem system = plugin.getSystem();
        int range = system.getUser(player).getActiveChannel().range();
        if (range < 0)
            return plugin.getConfig().getInt("DiceDefaultChatRange");
        return range;
    }
}
