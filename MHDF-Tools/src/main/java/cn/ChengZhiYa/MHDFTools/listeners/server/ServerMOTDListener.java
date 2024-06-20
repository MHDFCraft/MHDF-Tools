package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.Util.Placeholder;

public final class ServerMOTDListener implements Listener {

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMotd(Placeholder(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                Placeholder(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(MHDFTools.instance.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }

    @EventHandler
    public void onPaperServerListPing(PaperServerListPingEvent event) {
        event.setMotd(Placeholder(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                Placeholder(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(MHDFTools.instance.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }
}
