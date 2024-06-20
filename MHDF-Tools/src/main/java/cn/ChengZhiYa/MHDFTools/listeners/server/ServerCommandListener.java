package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import static cn.ChengZhiYa.MHDFTools.utils.Util.ifLogin;

public final class ServerCommandListener implements Listener {
    @EventHandler
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!ifLogin(event.getPlayer())) {
                String Command = event.getMessage().split(" ")[0];
                if (MHDFTools.instance.getConfig().getStringList("LoginSystemSettings.AllowUsedComamnds").contains(Command)) {
                    return;
                }
                event.setCancelled(true);
            }
        }
        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    return;
                }
            }
            String Command = event.getMessage().split(" ")[0];
            if (MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList").contains("/" + Command)) {
                event.setCancelled(true);
                for (String Message : MHDFTools.instance.getConfig().getStringList("BanCommandSettings.UsedBanCommandMessage")) {
                    event.getPlayer().sendMessage(MessageUtil.colorMessage(PlaceholderAPI.setPlaceholders(event.getPlayer(), Message)));
                }
            }
        }
    }

    @EventHandler
    public void PlayerCommandSendEvent(PlayerCommandSendEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getCommands().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getCommands().remove(BanCommand);
                }
            }
        }
    }

    @EventHandler
    public void PlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    for (String BanCommand : MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                        event.getTabCompletions().remove(BanCommand);
                    }
                }
            } else {
                for (String BanCommand : MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList")) {
                    event.getTabCompletions().remove(BanCommand);
                }
            }
        }
    }
}
