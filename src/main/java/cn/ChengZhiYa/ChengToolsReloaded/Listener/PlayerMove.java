package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.getLogin;

public class PlayerMove implements Listener {
    @EventHandler
    public void On_Event(PlayerMoveEvent event) {
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
        if (main.main.getConfig().getBoolean("FreezeCommandSettings.Enable")) {
            if (StringHashMap.Get(event.getPlayer().getName() + "_Freeze") != null) {
                event.setCancelled(true);
                if (main.main.getConfig().getBoolean("FreezeCommandSettings.FreezeMessageSettins.Enable")) {
                    event.getPlayer().sendMessage(ChatColor(event.getPlayer(),main.main.getConfig().getString("FreezeCommandSettings.FreezeMessageSettins.Message")));
                }
            }
        }
    }
}
