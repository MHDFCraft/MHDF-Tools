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
    public void AutoClose(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (MHDFTools.instance.getConfig().getStringList("FlySettings.AntiFlyWorldList").contains(player.getLocation().getWorld().getName())) {
            removeFlyTime(player.getName());
            InFlyList.remove(player.getName());
            player.setAllowFlight(false);
        }
    }

    @EventHandler
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ChangeWorld")) {
            Player player = event.getPlayer();
            if (InFlyList.contains(player.getName())) {
                player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReSpawn")) {
            Player player = event.getPlayer();
            if (MapUtil.getStringHasMap().get(player.getName() + "_Fly") != null) {
                player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReJoin")) {
            Player player = event.getPlayer();
            if (InFlyList.contains(player.getName())) {
                player.setAllowFlight(true);
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (!MHDFTools.instance.getConfig().getBoolean("FlySettings.AutoOpenSettings.ReJoin")) {
            Player player = event.getPlayer();
            InFlyList.remove(player.getName());
        }
    }
}
