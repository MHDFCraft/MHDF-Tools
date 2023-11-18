package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLogin;

public final class PlayerMove implements Listener {
    @EventHandler
    public void On_Event(PlayerMoveEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
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
