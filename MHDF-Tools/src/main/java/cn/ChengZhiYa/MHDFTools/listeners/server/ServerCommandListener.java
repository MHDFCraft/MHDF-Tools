package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;

public final class ServerCommandListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (plugin.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!ifLogin(event.getPlayer())) {
                String Command = event.getMessage().split(" ")[0];
                if (plugin.getConfig().getStringList("LoginSystemSettings.AllowUsedComamnds").contains(Command)) {
                    return;
                }
                event.setCancelled(true);
            }
        }
        if (plugin.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!plugin.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    return;
                }
            }
            String Command = event.getMessage().split(" ")[0];
            if (plugin.getConfig().getStringList("BanCommandSettings.BanCommandList").contains("/" + Command)) {
                event.setCancelled(true);
                for (String Message : plugin.getConfig().getStringList("BanCommandSettings.UsedBanCommandMessage")) {
                    event.getPlayer().sendMessage(MessageUtil.colorMessage(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message)));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCommandSendEvent(PlayerCommandSendEvent event) {
        if (plugin.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!plugin.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : plugin.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getCommands().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : plugin.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getCommands().remove(BanCommand);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event) {
        if (plugin.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!plugin.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : plugin.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getTabCompletions().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : plugin.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getTabCompletions().remove(BanCommand);
                }
            }
        }
    }
}
