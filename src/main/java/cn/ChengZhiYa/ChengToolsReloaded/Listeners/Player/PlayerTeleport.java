package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class PlayerTeleport implements Listener {
    @EventHandler
    public void onEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        LocationHasMap.getHasMap().put(player.getName() + "_TpBackLocation", player.getLocation());
    }
}
