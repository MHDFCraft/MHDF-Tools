package cn.ChengZhiYa.MHDFTools.command.subCommand.misc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.Placeholder;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class Say implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length >= 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args) {
                stringBuilder.append(arg).append(" ");
            }
            Bukkit.broadcast(Placeholder(null,stringBuilder.toString().replaceAll(args[0],"")),"MHDFTools.Broadcast");
        }else {
            sender.sendMessage(i18n("Usage.Say"));
        }
        return false;
    }
}
