package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;

public final class PlayerQuit implements Listener {
    @EventHandler
    public void On_Event(PlayerQuitEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            String QuitMessage = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("CustomQuitServerMessageSettings.QuitMessage")).replaceAll("%PlayerName%", player.getName());
            event.setQuitMessage(ChatColor(player, QuitMessage));
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            StringHasMap.getHasMap().put(player.getName() + "_Login", null);
        }
    }
}
