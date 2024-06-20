package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getJoinMessage;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getQuitMessage;

public final class ServerJoinLeaveMessageListener implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("MHDFTools.JoinMessage")) {
                event.setJoinMessage(getJoinMessage(player));
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("MHDFTools.QuitMessage")) {
                event.setQuitMessage(getQuitMessage(player));
            }
        }
    }
}
