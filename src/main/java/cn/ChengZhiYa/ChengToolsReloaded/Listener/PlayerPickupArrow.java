package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlayerPickupArrow implements Listener {
    @EventHandler
    public void On_Event(PlayerPickupArrowEvent event) {
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
