package cn.ChengZhiYa.ChengToolsReloaded.Event;

import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class PlayerQuit_Event implements Listener {
    @EventHandler
    public void On_Event(PlayerQuitEvent event) {
        if (main.main.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.isOp()) {
                String QuitMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomQuitServerMessageSettings.AdminQuitMessage")).replaceAll("%PlayerName%", player.getName());
                event.setQuitMessage(ChatColor(QuitMessage));
            } else {
                String QuitMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomQuitServerMessageSettings.PlayerQuitMessage")).replaceAll("%PlayerName%", player.getName());
                event.setQuitMessage(ChatColor(QuitMessage));
            }
        }
    }
}
