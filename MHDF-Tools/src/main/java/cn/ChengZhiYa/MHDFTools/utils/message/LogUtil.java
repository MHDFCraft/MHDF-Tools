package cn.ChengZhiYa.MHDFTools.utils.message;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import cn.ChengZhiYa.MHDFTools.MHDFTools;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

@UtilityClass
public class LogUtil {
    public Logger getLogger() {
        return MHDFTools.instance.getLogger();
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

    public void color(String Message) {
        CommandSender sender = Bukkit.getConsoleSender();
        if (MHDFPluginLoader.INSTANCE.getServerManager().is1_16orAbove()) {
            Message = MessageUtil.translateHexCodes(Message);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
        }
    }

        public void console ( final String info){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', info));
        }
    }
