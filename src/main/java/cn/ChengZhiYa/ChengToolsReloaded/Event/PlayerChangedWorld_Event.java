package cn.ChengZhiYa.ChengToolsReloaded.Event;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorld_Event implements Listener {
    @EventHandler
    public void On_Event(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (StringHashMap.Get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }
    }
}
