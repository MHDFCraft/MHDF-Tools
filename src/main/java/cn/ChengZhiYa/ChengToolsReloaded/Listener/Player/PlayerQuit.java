package cn.ChengZhiYa.ChengToolsReloaded.Listener.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlayerQuit implements Listener {
    @EventHandler
    public void On_Event(PlayerQuitEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.Op")) {
                java.lang.String QuitMessage = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("CustomQuitServerMessageSettings.AdminQuitMessage")).replaceAll("%PlayerName%", player.getName());
                event.setQuitMessage(ChatColor(player, QuitMessage));
            } else {
                java.lang.String QuitMessage = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("CustomQuitServerMessageSettings.PlayerQuitMessage")).replaceAll("%PlayerName%", player.getName());
                event.setQuitMessage(ChatColor(player, QuitMessage));
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            StringHasMap.getHasMap().put(player.getName() + "_Login", null);
        }
    }
}
