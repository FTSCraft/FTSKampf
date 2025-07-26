package de.ftscraft.ftskampf.spells.effects.effectDefinitions;

import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.entity.Player;

public abstract class RaceEffect implements Effect{

    protected boolean checkTargetRace(String raceName, Player target) {
        FTSKampf plugin = FTSKampf.getPlugin();
        return plugin.getRaceOrDefault(target).getmName().equals(raceName);
    }
}
