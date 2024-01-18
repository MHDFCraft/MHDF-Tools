package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getJoinMessage;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getQuitMessage;

public final class CustomJoinQuitMessage implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.JoinMessage")) {
                event.setJoinMessage(getJoinMessage(player));
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.QuitMessage")) {
                event.setQuitMessage(getQuitMessage(player));
            }
        }
    }
}
