package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.Utils.Util.PAPIChatColor;

public final class MOTD implements Listener {
    @EventHandler
    public void On_Event(PaperServerListPingEvent event) {
        event.setMotd(PAPIChatColor(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                PAPIChatColor(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(MHDFTools.instance.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }

    @EventHandler
    public void On_Event(ServerListPingEvent event) {
        event.setMotd(PAPIChatColor(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line1"))) + "\n" +
                PAPIChatColor(null, Objects.requireNonNull(MHDFTools.instance.getConfig().getString("MOTDSettings.Line2"))));
        event.setMaxPlayers(MHDFTools.instance.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }
}
