package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;


public class ServerListPing implements Listener {
    @EventHandler
    public void On_Event(ServerListPingEvent event) {
        event.setMotd(ChatColor(null, Objects.requireNonNull(main.main.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                ChatColor(null, Objects.requireNonNull(main.main.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(main.main.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }
}
