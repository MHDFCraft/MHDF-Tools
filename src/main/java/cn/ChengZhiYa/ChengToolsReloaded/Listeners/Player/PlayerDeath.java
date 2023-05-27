package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class PlayerDeath implements Listener {
    @EventHandler
    public void On_Event(PlayerDeathEvent event) {
        Player player = event.getEntity();
        LocationHasMap.getHasMap().put(player.getName() + "_DeathLocation", player.getLocation());
    }
}
