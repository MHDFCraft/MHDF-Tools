package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class InventoryClick implements Listener {
    @EventHandler
    public void On_Event(InventoryClickEvent event) {
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin((Player) event.getWhoClicked())) {
                event.setCancelled(true);
            }
        }
    }
}
