package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.command.subCommand.misc.freeze.Freeze;
import cn.ChengZhiYa.MHDFTools.hooks.PlaceholderAPI;
import me.clip.placeholderapi.libs.universalScheduler.foliaScheduler.FoliaScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerIp = Objects.requireNonNull(player.getAddress()).getHostString();

        if (Freeze.freezeUUID.contains(player.getUniqueId())
                && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("FreezeSettings.PunishEnable", true)) {
            String punishCommand = Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getString("FreezeSettings.PunishCommand"))
                    .replace("%player_name%", player.getDisplayName())
                    .replace("%player_ip%", getPlayerHost(playerIp));

              new FoliaScheduler(PluginLoader.INSTANCE.getPlugin()).runTask(() ->
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), punishCommand)
            );

            Freeze.freezeUUID.remove(player.getUniqueId());
        }
    }

    private String getPlayerHost(String s) {
        s = s.replaceFirst("/", "");
        int colonIndex = s.lastIndexOf(":");
        if (colonIndex <= 0) {
            return s;
        }
        return s.substring(0, colonIndex);
    }
}
