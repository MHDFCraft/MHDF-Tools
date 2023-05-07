package cn.ChengZhiYa.ChengToolsReloaded.Listener.Player;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public final class PlayerChangedWorld implements Listener {
    @EventHandler
    public void On_Event(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (StringHasMap.getHasMap().get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }
    }
}
