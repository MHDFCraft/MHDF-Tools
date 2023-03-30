package cn.ChengZhiYa.ChengToolsReloaded.Listener;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public class PlayerQuit implements Listener {
    @EventHandler
    public void On_Event(PlayerQuitEvent event) {
        if (main.main.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            if (player.hasPermission("ChengTools.Op")) {
                String QuitMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomQuitServerMessageSettings.AdminQuitMessage")).replaceAll("%PlayerName%", player.getName());
                event.setQuitMessage(ChatColor(player, QuitMessage));
            } else {
                String QuitMessage = Objects.requireNonNull(main.main.getConfig().getString("CustomQuitServerMessageSettings.PlayerQuitMessage")).replaceAll("%PlayerName%", player.getName());
                event.setQuitMessage(ChatColor(player, QuitMessage));
            }
        }
        if (main.main.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            Player player = event.getPlayer();
            StringHashMap.Set(player.getName() + "_Login", null);
        }
    }
}
