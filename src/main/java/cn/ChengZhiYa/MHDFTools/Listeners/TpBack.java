package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class TpBack implements Listener {
    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        LocationHasMap.getHasMap().put(player.getName() + "_TpBackLocation", player.getLocation());
    }
}
