package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.*;

public final class PlayerAllowedFlightListener implements Listener {
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getLocation().getWorld().getName();
        if (MHDFTools.instance.getConfig().getStringList("FlySettings.AntiFlyWorldList").contains(worldName)) {
            flyList.remove(player.getName());
            removeFly(player.getName());
            player.setAllowFlight(false);
        } else if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ChangeWorld")) {
            handleFlight(player);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReSpawn") &&
                MapUtil.getStringHashMap().containsKey(player.getName() + "_Fly")) {
            handleFlight(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReJoin") &&
                flyList.contains(player.getName())) {
            handleFlight(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReJoin") &&
                flyList.contains(event.getPlayer().getName())) {
            flyList.remove(event.getPlayer().getName());
            removeFly(event.getPlayer().getName());
        }
    }

    private void handleFlight(Player player) {
        if (getFlyTime(player.getName()) != -999) {
            if (getFlyTime(player.getName()) >= 0) {
                return;
            }
        }

        player.setAllowFlight(true);
        flyList.add(player.getName());
    }
}