package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.HashMap.StringHasMap;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.MHDFTools.Utils.Database.FlyUtil.InFlyList;
import static cn.ChengZhiYa.MHDFTools.Utils.Database.FlyUtil.removeFly;

public final class AutoFly implements Listener {
    @EventHandler
    public void AutoClose(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (MHDFTools.instance.getConfig().getStringList("FlySettings.AntiFlyWorldList").contains(player.getLocation().getWorld().getName())) {
            removeFly(player.getName());
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
            if (StringHasMap.getHasMap().get(player.getName() + "_Fly") != null) {
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
