package cn.ChengZhiYa.MHDFTools.util.message;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

@UtilityClass
public class LogUtil {
    public void info(final String info) {
        getLogger().info(info);
    }

    public void warn(final String warn) {
        getLogger().info(warn);
    }

    public void error(final String error) {
        getLogger().info(error);
    }
    public String ChatColor(String Message) {
        CommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageUtil.translateHexCodes(Message)));
        return Message;
    }
    public Logger getLogger() {
        return MHDFTools.instance.getLogger();
    }

    public void console(final String info) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', info));
    }

}
