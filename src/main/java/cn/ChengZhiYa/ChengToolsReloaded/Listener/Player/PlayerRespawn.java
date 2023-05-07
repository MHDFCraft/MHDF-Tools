package cn.ChengZhiYa.ChengToolsReloaded.Listener.Player;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHasMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlayerRespawn implements Listener {
    @EventHandler
    public void On_Event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (StringHasMap.getHasMap().get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }

        if (LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation") != null) {
            org.bukkit.Location DiedLocation = LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation");
            int X = DiedLocation.getBlockX();
            int Y = DiedLocation.getBlockY();
            int Z = DiedLocation.getBlockZ();
            player.sendMessage(getLang("Back.ReSpawn", java.lang.String.valueOf(X), java.lang.String.valueOf(Y), java.lang.String.valueOf(Z)));
        }
    }
}
