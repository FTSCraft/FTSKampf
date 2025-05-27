package de.ftscraft.ftskampf.damageCalculators;

import de.ftscraft.ftskampf.main.FTSKampf;
import de.ftscraft.ftskampf.utils.Dice;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DamageModifier {

    private final double helmetProportion = 0.2;
    private final double chestplateProportion = 0.35;
    private final double legginsProportion = 0.3;
    private final double bootProportion = 0.15;

    private FTSKampf plugin = FTSKampf.getPlugin();
    private FileConfiguration config = plugin.getConfig();

    private double axeDamageGain;
    private double axePrecisionLoss;
    private double crossbowPercisionLoss;

    private HashMap<Material, Double> attackModifierMelee = new HashMap();
    private HashMap<Material, Double> attackModifierDistance = new HashMap();
    private HashMap<Material, Double> defendModifier = new HashMap();

    private List<Material> precisionLossMelee = new ArrayList<>();
    private List<Material> precisionLossDistance = new ArrayList<>();

    protected DamageModifier() {

        double leatherArmor = config.getInt("Armor.Leather");
        defendModifier.put(Material.LEATHER_HELMET, (leatherArmor * helmetProportion) / 100);
        defendModifier.put(Material.LEATHER_CHESTPLATE, (leatherArmor * chestplateProportion) / 100);
        defendModifier.put(Material.LEATHER_LEGGINGS, (leatherArmor * legginsProportion) / 100);
        defendModifier.put(Material.LEATHER_BOOTS, (leatherArmor * bootProportion) / 100);

        double goldArmor = config.getInt("Armor.Gold");
        defendModifier.put(Material.GOLDEN_HELMET, (goldArmor * helmetProportion) / 100);
        defendModifier.put(Material.GOLDEN_CHESTPLATE, (goldArmor * chestplateProportion) / 100);
        defendModifier.put(Material.GOLDEN_LEGGINGS, (goldArmor * legginsProportion) / 100);
        defendModifier.put(Material.GOLDEN_BOOTS, (goldArmor * bootProportion) / 100);

        double chainArmor = config.getInt("Armor.Chain");
        defendModifier.put(Material.CHAINMAIL_HELMET, (chainArmor * helmetProportion) / 100);
        defendModifier.put(Material.CHAINMAIL_CHESTPLATE, (chainArmor * chestplateProportion) / 100);
        defendModifier.put(Material.CHAINMAIL_LEGGINGS, (chainArmor * legginsProportion) / 100);
        defendModifier.put(Material.CHAINMAIL_BOOTS, (chainArmor * bootProportion) / 100);

        double ironArmor = config.getInt("Armor.Iron");
        defendModifier.put(Material.IRON_HELMET, (ironArmor * helmetProportion) / 100);
        defendModifier.put(Material.IRON_CHESTPLATE, (ironArmor * chestplateProportion) / 100);
        defendModifier.put(Material.IRON_LEGGINGS, (ironArmor * legginsProportion) / 100);
        defendModifier.put(Material.IRON_BOOTS, (ironArmor * bootProportion) / 100);

        double diamondArmor = config.getInt("Armor.Diamond");
        defendModifier.put(Material.DIAMOND_HELMET, (diamondArmor * helmetProportion) / 100);
        defendModifier.put(Material.DIAMOND_CHESTPLATE, (diamondArmor * chestplateProportion) / 100);
        defendModifier.put(Material.DIAMOND_LEGGINGS, (diamondArmor * legginsProportion) / 100);
        defendModifier.put(Material.DIAMOND_BOOTS, (diamondArmor * bootProportion) / 100);

        double netheriteArmor = config.getInt("Armor.Netherite");
        defendModifier.put(Material.NETHERITE_HELMET, (netheriteArmor * helmetProportion) / 100);
        defendModifier.put(Material.NETHERITE_CHESTPLATE, (netheriteArmor * chestplateProportion) / 100);
        defendModifier.put(Material.NETHERITE_LEGGINGS, (netheriteArmor * legginsProportion) / 100);
        defendModifier.put(Material.NETHERITE_BOOTS, (netheriteArmor * bootProportion) / 100);

        defendModifier.put(Material.SHIELD, (config.getDouble("Armor.Shield")) / 100);

        axeDamageGain = config.getDouble("Weapon.Melee.AxeDamageGain") / 100;
        axePrecisionLoss = config.getDouble("Weapon.Melee.AxePrecisionLoss") / 100;

        precisionLossMelee.add(Material.WOODEN_AXE);
        precisionLossMelee.add(Material.STONE_AXE);
        precisionLossMelee.add(Material.GOLDEN_AXE);
        precisionLossMelee.add(Material.IRON_AXE);
        precisionLossMelee.add(Material.DIAMOND_AXE);
        precisionLossMelee.add(Material.NETHERITE_AXE);

        precisionLossDistance.add(Material.CROSSBOW);

        attackModifierMelee.put(Material.WOODEN_SWORD, config.getDouble("Weapon.Melee.Wood") / 100);
        attackModifierMelee.put(Material.WOODEN_AXE, (config.getDouble("Weapon.Melee.Wood") / 100) + axeDamageGain);

        attackModifierMelee.put(Material.STONE_SWORD, config.getDouble("Weapon.Melee.Stone") / 100);
        attackModifierMelee.put(Material.STONE_AXE, (config.getDouble("Weapon.Melee.Stone") / 100) + axeDamageGain);

        attackModifierMelee.put(Material.GOLDEN_SWORD, config.getDouble("Weapon.Melee.Gold") / 100);
        attackModifierMelee.put(Material.GOLDEN_AXE, (config.getDouble("Weapon.Melee.Gold") / 100) + axeDamageGain);

        attackModifierMelee.put(Material.IRON_SWORD, config.getDouble("Weapon.Melee.Iron") / 100);
        attackModifierMelee.put(Material.IRON_AXE, (config.getDouble("Weapon.Melee.Iron") / 100) + axeDamageGain);

        attackModifierMelee.put(Material.DIAMOND_SWORD, config.getDouble("Weapon.Melee.Diamond") / 100);
        attackModifierMelee.put(Material.DIAMOND_AXE, (config.getDouble("Weapon.Melee.Diamond") / 100) + axeDamageGain);

        attackModifierMelee.put(Material.NETHERITE_SWORD, config.getDouble("Weapon.Melee.Netherite") / 100);
        attackModifierMelee.put(Material.NETHERITE_AXE, (config.getDouble("Weapon.Melee.Netherite") / 100) + axeDamageGain);

        attackModifierMelee.put(Material.TRIDENT, config.getDouble("Weapon.Melee.Trident") / 100);

        crossbowPercisionLoss = config.getDouble("Weapon.Distance.CrossbowPrecisionLoss") / 100;

        attackModifierDistance.put(Material.BOW, config.getDouble("Weapon.Distance.Bow") / 100);
        attackModifierDistance.put(Material.CROSSBOW, config.getDouble("Weapon.Distance.Crossbow") / 100);
        attackModifierDistance.put(Material.TRIDENT, config.getDouble("Weapon.Distance.Trident") / 100);
    }

    protected int getModifiedAttack(int blankAttack, Dice dice, ItemStack item) {
        Material weapon = item.getType();
        if (dice.equals(Dice.MELEE)) {
            if (attackModifierMelee.containsKey(weapon)) {
                double attackModifier = blankAttack * attackModifierMelee.get(weapon);
                return blankAttack + (int) Math.round(attackModifier);
            }
        }
        if (dice.equals(Dice.DISTANCE)) {
            if (attackModifierDistance.containsKey(weapon)) {
                double attackModifier = blankAttack * attackModifierDistance.get(weapon);
                return blankAttack + (int) Math.round(attackModifier);
            }
        }
        return blankAttack;
    }

    protected int getModifiedDefend(int blankDefend, ItemStack[] armor, boolean hasShield, boolean penetrateArmor) {
        if(penetrateArmor) return 0;
        double defendPercentages = calculateArmorPercentage(armor);
        if (hasShield) {
            defendPercentages += defendModifier.get(Material.SHIELD);
        }
        double defendModifier = blankDefend * defendPercentages;
        return blankDefend - (int) Math.round(defendModifier);
    }

    protected int getModifiedHitchance(int blankHit, Dice dice, ItemStack item) {
        Material material = item.getType();
        if (dice.equals(Dice.MELEE)) {
            if (precisionLossMelee.contains(material)) {
                double precissionLoss = blankHit * axePrecisionLoss;
                return blankHit - (int) Math.round(precissionLoss);
            }
        }
        if (dice.equals(Dice.DISTANCE)) {
            if (precisionLossDistance.contains(material)) {
                double precissionLoss = blankHit * crossbowPercisionLoss;
                return blankHit - (int) Math.round(precissionLoss);
            }
        }
        return blankHit;
    }

    private double calculateArmorPercentage(ItemStack[] armor) {
        double defendPercentage = 0;
        for (ItemStack item : armor) {
            if (!(item == null)) {
                if (defendModifier.containsKey(item.getType())) {
                    defendPercentage += defendModifier.get(item.getType());
                }
            }
        }
        return defendPercentage;
    }

    public boolean isArmed(Player player) {
        return (getModifiedAttack(1, Dice.MELEE, player.getItemInHand()) > 1) || (getModifiedAttack(1, Dice.DISTANCE, player.getItemInHand()) > 1);
    }

}
