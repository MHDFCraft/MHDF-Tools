package cn.ChengZhiYa.ChengToolsReloaded.Listener.Server;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;


public final class ServerListPing implements Listener {
    @EventHandler
    public void On_Event(ServerListPingEvent event) {
        event.setMotd(ChatColor(null, Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                ChatColor(null, Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(ChengToolsReloaded.instance.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }
}
