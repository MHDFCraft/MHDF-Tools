package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;

public final class Freeze implements Listener {
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("FreezeCommandSettings.Enable")) {
            if (StringHasMap.getHasMap().get(event.getPlayer().getName() + "_Freeze") != null) {
                event.setCancelled(true);
                if (ChengToolsReloaded.instance.getConfig().getBoolean("FreezeCommandSettings.FreezeMessageSettins.Enable")) {
                    event.getPlayer().sendMessage(ChatColor(event.getPlayer(), ChengToolsReloaded.instance.getConfig().getString("FreezeCommandSettings.FreezeMessageSettins.Message")));
                }
            }
        }
    }
}
