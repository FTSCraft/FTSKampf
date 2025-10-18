package de.ftscraft.ftskampf.db;

import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfigManager {
    private final String SKILLS_COMPATIBLE_VERSION = "2.3.1.2";
    private final String HP_COMPATIBLE_VERSION = "2.2";
    private final String SPELLS_COMPATIBLE_VERSION = "2.2";
    private final String EFFECT_COMPATIBLE_VERSION = "2.2";

    private final FTSKampf plugin;
    private final FileConfiguration config;

    public ConfigManager() {
        this.plugin = FTSKampf.getPlugin();
        config = plugin.getConfig();
        checkConfig();
    }

    public void checkDatabaseVersions () {
        DBManager dbManager = plugin.getDB();
        HpManager hpManager = plugin.getHpManager();
        SpellManager spellManager = plugin.getSpellManager();
        EffectManager effectManager = plugin.getEffectManager();

        if(!config.getString("DBVersion.Skills").equals(SKILLS_COMPATIBLE_VERSION)) {
            dbManager.reset();
            config.set("DBVersion.Skills", SKILLS_COMPATIBLE_VERSION);
        }

        if(!config.getString("DBVersion.Hp").equals(HP_COMPATIBLE_VERSION)) {
            hpManager.reset();
            config.set("DBVersion.Hp", HP_COMPATIBLE_VERSION);
        }

        if(!config.getString("DBVersion.Spells").equals(SPELLS_COMPATIBLE_VERSION)) {
            spellManager.reset();
            config.set("DBVersion.Spells", SPELLS_COMPATIBLE_VERSION);
        }

        if(!config.getString("DBVersion.Effect").equals(EFFECT_COMPATIBLE_VERSION)) {
            effectManager.reset();
            config.set("DBVersion.Effect", EFFECT_COMPATIBLE_VERSION);
        }

        if(!config.getString("DBVersion.Effect").equals(EFFECT_COMPATIBLE_VERSION)) {
            effectManager.reset();
            config.set("DBVersion.Effect", EFFECT_COMPATIBLE_VERSION);
        }
        plugin.saveConfig();
    }

    private void checkConfig() {
        HashMap<String, Object> defaultValues = new HashMap<>();
        defaultValues.put("Testmode", false);
        defaultValues.put("Permissions.UseDices.Name", "ftskampf.useDices");
        defaultValues.put("Permissions.UseDices.Required", true);
        defaultValues.put("Permissions.SetSkills.Name", "ftskampf.setSkills");
        defaultValues.put("Permissions.SetSkills.Required", true);
        defaultValues.put("Permissions.ResetSkills.Name", "ftskampf.resetSkills");
        defaultValues.put("Permissions.ResetSkills.Required", true);
        defaultValues.put("Permissions.ResetOtherSkills.Name", "ftskampf.resetOtherSkills");
        defaultValues.put("Permissions.ResetOtherSkills.Required", true);
        defaultValues.put("Permissions.Heal.Name", "ftskampf.heal");
        defaultValues.put("Permissions.Heal.Required", true);
        defaultValues.put("Permissions.DBAdmin.Name", "ftskampf.dbadmin");
        defaultValues.put("Permissions.DBAdmin.Required", true);
        defaultValues.put("Permissions.SetMaxSkillPoints.Name", "ftskampf.setMaxSkillPoints");
        defaultValues.put("Permissions.SetMaxSkillPoints.Required", true);

        defaultValues.put("Permissions.ChooseSpells.Name", "ftskampf.chooseSpells");
        defaultValues.put("Permissions.ChooseSpells.Required", true);
        defaultValues.put("Permissions.ResetSpells.Name", "ftskampf.resetSpells");
        defaultValues.put("Permissions.ResetSpells.Required", true);
        defaultValues.put("Permissions.ResetSpellsOther.Name", "ftskampf.resetOtherSpells");
        defaultValues.put("Permissions.ResetSpellsOther.Required", true);

        defaultValues.put("MaxSpells", 4);
        defaultValues.put("EffectLifetime", 30);

        defaultValues.put("DiceDefaultChatRange", 20);

        defaultValues.put("Dice.Melee.Max", 100);
        defaultValues.put("Dice.Melee.MaxSkillable", 100);
        defaultValues.put("Dice.Distance.Max", 100);
        defaultValues.put("Dice.Distance.MaxSkillable", 100);
        defaultValues.put("Dice.Magic.Max", 100);
        defaultValues.put("Dice.Magic.MaxSkillable", 100);
        defaultValues.put("Dice.Agility.Max", 100);
        defaultValues.put("Dice.Agility.MaxSkillable", 50);
        defaultValues.put("Dice.Action.Max", 100);

        defaultValues.put("Health.RegenRate", 30);
        defaultValues.put("Health.RegenPoints", 1);
        defaultValues.put("Health.SkillMultiplier", 5);
        defaultValues.put("Health.HealtimeOffsetMinutes", 30);
        defaultValues.put("MinimumDamage", 5);

        defaultValues.put("Armor.Leather", 20);
        defaultValues.put("Armor.Gold", 25);
        defaultValues.put("Armor.Chain", 25);
        defaultValues.put("Armor.Iron", 35);
        defaultValues.put("Armor.Diamond", 55);
        defaultValues.put("Armor.Netherite", 70);
        defaultValues.put("Agility.Leather", 5);
        defaultValues.put("Agility.Gold", 10);
        defaultValues.put("Agility.Chain", 10);
        defaultValues.put("Agility.Iron", 25);
        defaultValues.put("Agility.Diamond", 40);
        defaultValues.put("Agility.Netherite", 50);

        defaultValues.put("Armor.Shield", 8);

        defaultValues.put("Weapon.Melee.Wood", 10);
        defaultValues.put("Weapon.Melee.Stone", 15);
        defaultValues.put("Weapon.Melee.Gold", 20);
        defaultValues.put("Weapon.Melee.Iron", 35);
        defaultValues.put("Weapon.Melee.Diamond", 50);
        defaultValues.put("Weapon.Melee.Netherite", 65);
        defaultValues.put("Weapon.Melee.Trident", 40);

        defaultValues.put("Weapon.Melee.AxeDamageGain", 5);
        defaultValues.put("Weapon.Melee.AxePrecisionLoss", 10);

        defaultValues.put("Weapon.Distance.Bow", 30);
        defaultValues.put("Weapon.Distance.Crossbow", 45);
        defaultValues.put("Weapon.Distance.Trident", 40);

        defaultValues.put("Weapon.Distance.CrossbowPrecisionLoss", 10);

        defaultValues.put("Races.Default", "Mensch");
        defaultValues.put("Races.List", new String[]{"Mensch", "Elf", "Zwerg", "Ork"});

        defaultValues.put("Races.Mensch.MName", "Mensch");
        defaultValues.put("Races.Mensch.FName", "Menschenfrau");
        defaultValues.put("Races.Mensch.points", 100);
        defaultValues.put("Races.Mensch.InitialValues.Melee", 10);
        defaultValues.put("Races.Mensch.InitialValues.Distance", 15);
        defaultValues.put("Races.Mensch.InitialValues.Magic", -1);
        defaultValues.put("Races.Mensch.InitialValues.Agility", 8);
        defaultValues.put("Races.Mensch.InitialValues.Health", 100);

        defaultValues.put("Races.Elf.MName", "Elf");
        defaultValues.put("Races.Elf.FName", "Elfe");
        defaultValues.put("Races.Elf.points", 120);
        defaultValues.put("Races.Elf.InitialValues.Melee", 8);
        defaultValues.put("Races.Elf.InitialValues.Distance", 10);
        defaultValues.put("Races.Elf.InitialValues.Magic", 15);
        defaultValues.put("Races.Elf.InitialValues.Agility", 9);
        defaultValues.put("Races.Elf.InitialValues.Health", 80);

        defaultValues.put("Races.Zwerg.MName", "Zwerg");
        defaultValues.put("Races.Zwerg.FName", "Zwergin");
        defaultValues.put("Races.Zwerg.points", 120);
        defaultValues.put("Races.Zwerg.InitialValues.Melee", 12);
        defaultValues.put("Races.Zwerg.InitialValues.Distance", 12);
        defaultValues.put("Races.Zwerg.InitialValues.Magic", 8);
        defaultValues.put("Races.Zwerg.InitialValues.Agility", 7);
        defaultValues.put("Races.Zwerg.InitialValues.Health", 120);

        defaultValues.put("Races.Ork.MName", "Ork");
        defaultValues.put("Races.Ork.FName", "Orkin");
        defaultValues.put("Races.Ork.points", 120);
        defaultValues.put("Races.Ork.InitialValues.Melee", 18);
        defaultValues.put("Races.Ork.InitialValues.Distance",5);
        defaultValues.put("Races.Ork.InitialValues.Magic", 5);
        defaultValues.put("Races.Ork.InitialValues.Agility", 6);
        defaultValues.put("Races.Ork.InitialValues.Health", 150);

        defaultValues.put("DBVersion.Skills", "0");
        defaultValues.put("DBVersion.Hp", "0");
        defaultValues.put("DBVersion.Spells", "0");
        defaultValues.put("DBVersion.Effect", "0");

        defaultValues.put("SPELL_DAMAGE_MODIFIER", 1.5);
        defaultValues.put("SPELL_BUFFAGILITY_MODIFIER", 1.2);
        defaultValues.put("SPELL_BUFFAGILITY_DURABILITY", 3);
        defaultValues.put("SPELL_BUFFARMOR_MODIFIER", 0.7);
        defaultValues.put("SPELL_BUFFARMOR_DURABILITY", 3);
        defaultValues.put("SPELL_BUFFATTACK_MODIFIER", 1.5);
        defaultValues.put("SPELL_BUFFATTACKARMORRANGE_MODIFIER_ATTACK", 1.2);
        defaultValues.put("SPELL_BUFFATTACKARMORRANGE_MODIFIER_ARMOR", 0.8);
        defaultValues.put("SPELL_BUFFATTACKDEBUFFARMOR_MODIFIER_ATTACK", 1.2);
        defaultValues.put("SPELL_BUFFATTACKDEBUFFARMOR_MODIFIER_ARMOR", 1.3);
        defaultValues.put("SPELL_BBUFFATTACKARMORRANG_RANGE", 5);
        defaultValues.put("SPELL_BUFFUNARMED_MODIFIER", 1.8);
        defaultValues.put("SPELL_BUFFUNARMED_DURABILITY", 3);
        defaultValues.put("SPELL_DAMAGEABSORPTION_MODIFIER", 1.2);
        defaultValues.put("SPELL_DAMAGEABSORPTION_ABSORPTIONRATE", 0.5);
        defaultValues.put("SPELL_DAMAGEOVERTIME_DURABILTY", 3);
        defaultValues.put("SPELL_DAMAGEPENETRATION_MODIFIER", 0.8);
        defaultValues.put("SPELL_DEBUFFTARGETAGILITY_MODIFIER", 0.7);
        defaultValues.put("SPELL_DEBUFFTARGETAGILITY_DURABILITY", 3);
        defaultValues.put("SPELL_DEBUFFTARGETARMOR_MODIFIER", 1.3);
        defaultValues.put("SPELL_DEBUFFTARGETARMOR_DURABILITY", 3);
        defaultValues.put("SPELL_DEBUFFTARGETATTACK_MODIFIER", 0.7);
        defaultValues.put("SPELL_DEBUFFTARGETATTACK_DURABILITY", 3);
        defaultValues.put("SPELL_HEALIMITRANGE_MODIFIER", 1.5);
        defaultValues.put("SPELL_HEALLIMITRANGE_RANGE", 2);
        defaultValues.put("SPELL_HEALRADIUS_MODIFIER", 0.5);
        defaultValues.put("SPELL_HEALRADIUS_RANGE", 5);
        defaultValues.put("SPELL_HEALSELFDAMAGE_HEALMODIFIER", 1.0);
        defaultValues.put("SPELL_HEALSELFDAMAGE_DAMAGEMODIFIER", 0.5);
        defaultValues.put("SPELL_PROTECTATTACK_DURABILITY", 1);
        defaultValues.put("SPELL_RACEBUFFARMORRADIUS_MODIFIER", 0.7);
        defaultValues.put("SPELL_RACEBUFFARMORRADIUS_RANGE", 5);
        defaultValues.put("SPELL_RACEBUFFATTACKRADIUS_MODIFIER", 1.5);
        defaultValues.put("SPELL_RACEBUFFATTACKRADIUS_RANGE", 5);
        defaultValues.put("SPELL_RACEDAMAGE_MODIFIER", 1.8);
        defaultValues.put("SPELL_RACEHEAL_MODIFIER", 1.3);
        defaultValues.put("SPELL_RACEHEALRADIUS_MODIFIER", 0.7);
        defaultValues.put("SPELL_RACEHEALRADIUS_RANGE", 5);
        defaultValues.put("SPELL_DAMAGENOCKBACK_MODIFIER", 1.4);
        defaultValues.put("SPELL_PROTECTATTACKSPELL_MODIFIER", 0.5);
        defaultValues.put("SPELL_STUNSPELL_BUFFARMOR_MODIFIER", 0.7);
        defaultValues.put("SPELL_STUNSPELL_BUFFARMOR_DURABILITY", 1);


        for(String key : defaultValues.keySet()) {
            if(!config.isSet(key)) config.set(key, defaultValues.get(key));
        }

        plugin.saveConfig();
    }
}
