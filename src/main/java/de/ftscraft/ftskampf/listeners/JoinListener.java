package de.ftscraft.ftskampf.listeners;

import de.ftscraft.ftskampf.db.HpManager;
import de.ftscraft.ftskampf.main.FTSKampf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    FTSKampf plugin = FTSKampf.getPlugin();
    HpManager hpManager = plugin.getHpManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        hpManager.registerActivePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        hpManager.unregisterActivePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        hpManager.unregisterActivePlayer(event.getPlayer());
    }
}
