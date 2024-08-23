package cn.ChengZhiYa.MHDFTools.utils.message;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

@UtilityClass
public class ColorLogs {
    public Logger getLogger() {
        return PluginLoader.INSTANCE.getPlugin().getLogger();
    }

    public void info(final String info) {
        getLogger().info(info);
    }

    public void warn(final String warn) {
        getLogger().info(warn);
    }

    public void error(final String error) {
        getLogger().info(error);
    }

    public void debug(String... messages) {
        if (PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("Debug")) {
            StringBuilder messageBuilder = new StringBuilder("[MHDF-Tools-调试] ");
            for (String message : messages) {
                messageBuilder.append(message);
                if (!message.equals(messages[messages.length - 1])) {
                    messageBuilder.append(" | ");
                }
            }
            color(messageBuilder.toString());
        }
    }

    public void color(String Message) {
        CommandSender sender = Bukkit.getConsoleSender();
        if (PluginLoader.INSTANCE.getServerManager().is1_16orAbove()) {
            Message = MessageUtil.translateHexCodes(Message);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
        }
    }

    public void console(final String info) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', info));
    }
}
