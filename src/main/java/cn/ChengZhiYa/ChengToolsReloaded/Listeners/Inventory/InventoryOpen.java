package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Inventory;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class InventoryOpen implements Listener {
    @EventHandler
    public void On_event(InventoryOpenEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin((Player) event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }
}
