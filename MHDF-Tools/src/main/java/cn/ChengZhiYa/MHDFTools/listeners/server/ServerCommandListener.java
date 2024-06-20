package cn.ChengZhiYa.MHDFTools.listeners.server;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.message.MessageUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

import java.util.List;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.ifLogin;

public final class ServerCommandListener implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("LoginSystemSettings.Enable")) {
            if (!ifLogin(event.getPlayer())) {
                String command = event.getMessage().split(" ")[0];
                if (MHDFTools.instance.getConfig().getStringList("LoginSystemSettings.AllowUsedComamnds").contains(command)) {
                    return;
                }
                event.setCancelled(true);
                return;
            }
        }

        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            String command = event.getMessage().split(" ")[0];
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass")) {
                if (!MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                    return;
                }
            }
            if (MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList").contains("/" + command)) {
                event.setCancelled(true);
                for (String message : MHDFTools.instance.getConfig().getStringList("BanCommandSettings.UsedBanCommandMessage")) {
                    event.getPlayer().sendMessage(MessageUtil.colorMessage(PlaceholderAPI.setPlaceholders(event.getPlayer(), message)));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCommandSend(PlayerCommandSendEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass") && !MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                removeCommandsFromList(event, MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList"));
            } else {
                removeCommandsFromList(event, MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList"));
            }
        }
    }

    private void removeCommandsFromList(PlayerCommandSendEvent event, List<String> banCommandList) {
        List<String> commands = (List<String>) event.getCommands();
        commands.removeIf(command -> banCommandList.contains("/" + command));
    }


    @EventHandler
    public void onPlayerChatTabComplete(PlayerChatTabCompleteEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.Enable")) {
            if (event.getPlayer().hasPermission("MHDFTools.BanCommand.Bypass") && !MHDFTools.instance.getConfig().getBoolean("BanCommandSettings.OpBypass")) {
                removeTabCompletions(event, MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList"));
            } else {
                removeTabCompletions(event, MHDFTools.instance.getConfig().getStringList("BanCommandSettings.BanCommandList"));
            }
        }
    }

    private void removeTabCompletions(PlayerChatTabCompleteEvent event, List<String> banCommandList) {
        List<String> tabCompletions = (List<String>) event.getTabCompletions();
        tabCompletions.removeIf(banCommandList::contains);
    }
}