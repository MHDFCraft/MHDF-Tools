package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;

public final class JoinTeleportSpawn implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomJoinServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            String JoinMessage = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("CustomJoinServerMessageSettings.JoinMessage")).replaceAll("%PlayerName%", player.getName());
            event.setJoinMessage(ChatColor(player, JoinMessage));
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("CustomQuitServerMessageSettings.Enable")) {
            Player player = event.getPlayer();
            String QuitMessage = Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("CustomQuitServerMessageSettings.QuitMessage")).replaceAll("%PlayerName%", player.getName());
            event.setQuitMessage(ChatColor(player, QuitMessage));
        }
    }
}
