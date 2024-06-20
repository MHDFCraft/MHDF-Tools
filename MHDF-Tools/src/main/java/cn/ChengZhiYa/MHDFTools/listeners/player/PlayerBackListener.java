package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.MHDFTools.utils.Util.i18n;

public final class PlayerBackListener implements Listener {
    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MapUtil.getLocationHasMap().put(player.getName() + "_DeathLocation", player.getLocation());
    }

    @EventHandler
    public void On_Event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (MapUtil.getLocationHasMap().get(player.getName() + "_DeathLocation") != null) {
            org.bukkit.Location DiedLocation = MapUtil.getLocationHasMap().get(player.getName() + "_DeathLocation");
            int X = DiedLocation.getBlockX();
            int Y = DiedLocation.getBlockY();
            int Z = DiedLocation.getBlockZ();
            player.sendMessage(i18n("Back.ReSpawn", String.valueOf(X), String.valueOf(Y), String.valueOf(Z)));
        }
    }
}
