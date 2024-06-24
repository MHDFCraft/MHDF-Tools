package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;

public final class ServerMOTDListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(Placeholder(null, Objects.requireNonNull(plugin.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                Placeholder(null, Objects.requireNonNull(plugin.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(plugin.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }

    @EventHandler
    public void onPaperServerListPing(PaperServerListPingEvent event) {
        event.setMotd(Placeholder(null, Objects.requireNonNull(plugin.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                Placeholder(null, Objects.requireNonNull(plugin.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(plugin.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }
}
