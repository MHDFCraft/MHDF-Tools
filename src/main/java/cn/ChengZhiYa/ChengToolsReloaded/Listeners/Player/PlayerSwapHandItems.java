package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlayerSwapHandItems implements Listener {
    @EventHandler
    public void On_Event(PlayerSwapHandItemsEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
