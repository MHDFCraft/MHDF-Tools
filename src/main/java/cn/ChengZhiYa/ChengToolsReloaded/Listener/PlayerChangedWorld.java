package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorld implements Listener {
    @EventHandler
    public void On_Event(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (StringHashMap.Get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }
    }
}
