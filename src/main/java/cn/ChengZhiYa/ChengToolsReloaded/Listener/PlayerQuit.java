package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class PlayerQuit implements Listener {
    @EventHandler
    public void On_Event(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (main.main.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            if (player.hasPermission("ChengTools.Op")) {
                String QuitMessage = PlaceholderAPI.setPlaceholders(player, Objects.requireNonNull(main.main.getConfig().getString("CustomQuitServerMessageSettings.AdminQuitMessage")).replaceAll("%PlayerName%", player.getName()));
                event.setQuitMessage(ChatColor(QuitMessage));
            } else {
                String QuitMessage = PlaceholderAPI.setPlaceholders(player, Objects.requireNonNull(main.main.getConfig().getString("CustomQuitServerMessageSettings.PlayerQuitMessage")).replaceAll("%PlayerName%", player.getName()));
                event.setQuitMessage(ChatColor(QuitMessage));
            }
        }
        StringHashMap.Set(player.getName() + "_Login", null);
    }
}
