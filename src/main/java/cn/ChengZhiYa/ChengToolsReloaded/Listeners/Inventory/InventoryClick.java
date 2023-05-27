package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Inventory;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class InventoryClick implements Listener {
    @EventHandler
    public void On_Event(InventoryClickEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin((Player) event.getWhoClicked())) {
                event.setCancelled(true);
            }
        }
    }
}
