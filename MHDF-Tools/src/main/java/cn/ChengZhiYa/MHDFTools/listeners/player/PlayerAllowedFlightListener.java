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

import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.InFlyList;
import static cn.ChengZhiYa.MHDFTools.utils.database.FlyUtil.removeFlyTime;

public final class PlayerAllowedFlightListener implements Listener {
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getLocation().getWorld().getName();
        if (MHDFTools.instance.getConfig().getStringList("FlySettings.AntiFlyWorldList").contains(worldName)) {
            removeFlyTime(player.getName());
            InFlyList.remove(player.getName());
            player.setAllowFlight(false);
        } else if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ChangeWorld")) {
            handleFlight(player);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReSpawn") &&
                MapUtil.getStringHasMap().containsKey(player.getName() + "_Fly")) {
            handleFlight(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReJoin") &&
                InFlyList.contains(player.getName())) {
            handleFlight(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReJoin")) {
            Player player = event.getPlayer();
            InFlyList.remove(player.getName());
        }
    }

    private void handleFlight(Player player) {
        player.setAllowFlight(true);
    }
}