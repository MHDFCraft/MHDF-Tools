package cn.ChengZhiYa.ChengToolsReloaded.Listeners.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.ChatColor;
import static cn.ChengZhiYa.ChengToolsReloaded.Utils.Util.getLogin;

public final class PlayerCommandPreprocess implements Listener {
    @EventHandler
    public void On_Event(PlayerCommandPreprocessEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                String Command = event.getMessage().split(" ")[0];
                if (ChengToolsReloaded.instance.getConfig().getStringList("LoginSystemSettings.AllowUsedComamnds").contains(Command)) {
                    return;
                }
                event.setCancelled(true);
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("ChengTools.BanCommand.Bypass")) {
                if (!ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    return;
                }
            }
            String Command = event.getMessage().split(" ")[0];
            if (ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList").contains("/" + Command)) {
                event.setCancelled(true);
                for (String Message : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.UsedBanCommandMessage")) {
                    event.getPlayer().sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message)));
                }
            }
        }
    }
}
