package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlayerRespawn implements Listener {
    @EventHandler
    public void On_Event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (StringHashMap.Get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }

        if (LocationHashMap.Get(player.getName() + "_DeathLocation") != null) {
            Location DiedLocation = LocationHashMap.Get(player.getName() + "_DeathLocation");
            int X = DiedLocation.getBlockX();
            int Y = DiedLocation.getBlockY();
            int Z = DiedLocation.getBlockZ();
            player.sendMessage(getLang("Back.ReSpawn",String.valueOf(X),String.valueOf(Y), String.valueOf(Z)));
        }
    }
}
