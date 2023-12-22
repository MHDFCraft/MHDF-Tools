package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Entity;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLogin;

public final class EntityPickupItem implements Listener {
    @EventHandler
    public void On_Event(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
                if (!getLogin(((Player) event.getEntity()))) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
