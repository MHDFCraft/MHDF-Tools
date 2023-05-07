package cn.ChengZhiYa.ChengToolsReloaded.Listener.Player;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.*;

public final class PlayerCommandPreprocess implements Listener {
    @EventHandler
    public void On_Event(PlayerCommandPreprocessEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!getLogin(event.getPlayer())) {
                String Command = event.getMessage().split(" ")[0].toLowerCase(Locale.ROOT);
                for (String AllowCommand : ChengToolsReloaded.instance.getConfig().getStringList("LoginSystemSettings.AllowUsedComamnds")) {
                    if (Command.equals(AllowCommand.toLowerCase(Locale.ROOT))) {
                        return;
                    }
                }
                event.setCancelled(true);
            }
        }
        if (ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("ChengTools.BanCommand.Bypass")) {
                if (!ChengToolsReloaded.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    String Command = event.getMessage().split(" ")[0].toLowerCase(Locale.ROOT);
                    for (String BanCommand : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        if (Command.equals("/" + BanCommand.toLowerCase(Locale.ROOT))) {
                            event.setCancelled(true);
                            for (String Message : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.UsedBanCommandMessage")) {
                                event.getPlayer().sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message)));
                            }
                            return;
                        }
                    }
                }
            } else {
                String Command = event.getMessage().split(" ")[0].toLowerCase(Locale.ROOT);
                for (String BanCommand : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    if (Command.equals("/" + BanCommand.toLowerCase(Locale.ROOT))) {
                        event.setCancelled(true);
                        for (String Message : ChengToolsReloaded.instance.getConfig().getStringList("BanCommandSettings.UsedBanCommandMessage")) {
                            event.getPlayer().sendMessage(ChatColor(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message)));
                        }
                        return;
                    }
                }
            }
        }
    }
}
