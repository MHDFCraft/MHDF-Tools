package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getJoinMessage;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.getQuitMessage;

public final class ServerJoinLeaveMessageListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("MHDFTools.JoinMessage")) {
                event.setJoinMessage(getJoinMessage(player));
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (plugin.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("MHDFTools.QuitMessage")) {
                event.setQuitMessage(getQuitMessage(player));
            }
        }
    }
}
