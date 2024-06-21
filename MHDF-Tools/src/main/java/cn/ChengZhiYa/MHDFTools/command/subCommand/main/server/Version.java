package cn.ChengZhiYa.MHDFTools.command.subCommand.main.server;

import cn.ChengZhiYa.MHDFTools.MHDFPluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Version implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length > 0) {
            return false;
        }
        sender.sendMessage("§f============§6梦之工具§f============");
        sender.sendMessage("§7Version: §6" + MHDFPluginLoader.INSTANCE.getVersion());
        sender.sendMessage("§7Built: §6" + MHDFPluginLoader.INSTANCE.getBuild());
        sender.sendMessage("§7Server: §6" + MHDFPluginLoader.INSTANCE.getServerManager().getVersion());
        sender.sendMessage("§f============§6梦之工具§f============");
        return true;
    }
}