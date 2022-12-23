package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;


public class ServerListPing implements Listener {
    @EventHandler
    public void On_Event(ServerListPingEvent event) {
        event.setMotd(ChatColor(PlaceholderAPI.setPlaceholders(null,Objects.requireNonNull(main.main.getConfig().getString("MotdSettings.Line1")))) + "\n" + ChatColor(PlaceholderAPI.setPlaceholders(null,Objects.requireNonNull(main.main.getConfig().getString("MotdSettings.Line2")))));
        event.setMaxPlayers(main.main.getConfig().getInt("MotdSettings.MaxPlayers"));
    }
}
