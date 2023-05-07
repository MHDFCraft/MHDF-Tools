package cn.ChengZhiYa.ChengToolsReloaded.Listener.Entity;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class EntityDamageByEntity implements Listener {
    @EventHandler
    public void On_Event(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
                if (!getLogin(((Player) event.getEntity()))) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
