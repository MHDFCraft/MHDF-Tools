package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class PaperServerListPing  implements Listener {
    @EventHandler
    public void On_Event(PaperServerListPingEvent event) {
        event.setMotd(ChatColor(PlaceholderAPI.setPlaceholders(null,Objects.requireNonNull(main.main.getConfig().getString("MOTDSettings.Line1")))) + "\n" + ChatColor(PlaceholderAPI.setPlaceholders(null,Objects.requireNonNull(main.main.getConfig().getString("MOTDSettings.Line2")))));
        event.setMaxPlayers(main.main.getConfig().getInt("MOTDSettings.MaxPlayers"));
    }
}