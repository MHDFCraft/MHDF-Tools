package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class AutoFly implements Listener {
    @EventHandler
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (StringHasMap.getHasMap().get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (StringHasMap.getHasMap().get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }
    }
}
