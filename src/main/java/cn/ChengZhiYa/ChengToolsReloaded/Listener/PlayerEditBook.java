package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlayerEditBook implements Listener {
    @EventHandler
    public void On_Event(PlayerEditBookEvent event) {
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
