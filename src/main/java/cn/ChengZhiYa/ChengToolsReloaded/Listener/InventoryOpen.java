package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class InventoryOpen implements Listener {
    @EventHandler
    public void On_event(InventoryOpenEvent event) {
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin((Player) event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
